package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.SessionParam;

import java.util.Arrays;
import java.util.EnumSet;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import apdu4j.HexUtils;
import apdu4j.ISO7816;
import pro.javacard.gp.GPCrypto;
import pro.javacard.gp.GPData;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GPUtils;
import pro.javacard.gp.GlobalPlatform;
import pro.javacard.gp.SessionKeyProvider;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class ExternAuthCmd extends AbsGPCmd<SessionParam, Integer> {
    private byte[] diversification_data = null;

    // TODO scpMajorVersion 是否要保存下来
    @Override
    public CommandAPDU onReq(SessionParam sessionParam) throws CardException, GPException {
        SessionKeyProvider keys = sessionParam.getKeys();
        byte[] host_challenge = sessionParam.getHost_challenge();
        EnumSet<GlobalPlatform.APDUMode> securityLevel = sessionParam.getSecurityLevel();
        int scpVersion = sessionParam.getScpVersion();
        byte[] update_response = sessionParam.getTmpAPDU().getData();

        // Verify response length (SCP01/SCP02 + SCP03 + SCP03 w/ pseudorandom)
        if (update_response.length != 28 && update_response.length != 29 && update_response.length != 32) {
            throw new GPException("Invalid INITIALIZE UPDATE response length: " + update_response.length);
        }
        // Parse the response
        int offset = 0;
        diversification_data = Arrays.copyOfRange(update_response, 0, 10);
        offset += diversification_data.length;
        // Get used key version from response
        int keyVersion = update_response[offset] & 0xFF;
        offset++;
        // Get major SCP version from Key Information field in response
        int scpMajorVersion = update_response[offset];
        offset++;
        mGPCtx.setScpMajorVersion(scpMajorVersion);
        // get the protocol "i" parameter, if SCP03
        int scp_i = -1;
        if (scpMajorVersion == 3) {
            scp_i = update_response[offset];
            offset++;
        }

        // FIXME: SCP02 has 2 byte sequence + 6 bytes card challenge but the challenge is discarded.
        // get card challenge
        byte card_challenge[] = Arrays.copyOfRange(update_response, offset, offset + 8);
        offset += card_challenge.length;
        // get card cryptogram
        byte card_cryptogram[] = Arrays.copyOfRange(update_response, offset, offset + 8);
        offset += card_cryptogram.length;

        logger.debug("Host challenge: " + HexUtils.bin2hex(host_challenge));
        logger.debug("Card challenge: " + HexUtils.bin2hex(card_challenge));

        // Verify response
        // If using explicit key version, it must match.
        if ((keys.getKeysetVersion() > 0) && (keyVersion != keys.getKeysetVersion())) {
            throw new GPException("Key version mismatch: " + keys.getKeysetVersion() + " != " + keyVersion);
        }

        logger.debug("Card reports SCP0" + scpMajorVersion + " with version " + keyVersion + " keys");

        // Set default SCP version based on major version, if not explicitly known.
        if (scpVersion == SCP_ANY) {
            if (scpMajorVersion == 1) {
                scpVersion = SCP_01_05;
            } else if (scpMajorVersion == 2) {
                scpVersion = SCP_02_15;
            } else if (scpMajorVersion == 3) {
                logger.debug("SCP03 i=" + scp_i);
                scpVersion = 3; // FIXME: the symbolic numbering of versions needs to be fixed.
            }
        } else if (scpVersion != scpMajorVersion) {
            logger.debug("Overriding SCP version: card reports " + scpMajorVersion + " but user requested " + scpVersion);
            scpMajorVersion = scpVersion;
            if (scpVersion == 1) {
                scpVersion = SCP_01_05;
            } else if (scpVersion == 2) {
                scpVersion = SCP_02_15;
            } else {
                logger.debug("error: " + scpVersion);
            }
        }

        // Remove RMAC if SCP01 TODO: this should be generic sanitizer somewhere
        if (scpMajorVersion == 1 && securityLevel.contains(GlobalPlatform.APDUMode.RMAC)) {
            logger.debug("SCP01 does not support RMAC, removing.");
            securityLevel.remove(GlobalPlatform.APDUMode.RMAC);
        }

        // Derive session keys
        byte[] seq = null;
        if (scpMajorVersion == 1) {
            mGPCtx.setSessionKeys(keys.getSessionKeys(scpMajorVersion, diversification_data, host_challenge, card_challenge));
        } else if (scpMajorVersion == 2) {
            seq = Arrays.copyOfRange(update_response, 12, 14);
            mGPCtx.setSessionKeys(keys.getSessionKeys(2, diversification_data, seq));
        } else if (scpMajorVersion == 3) {
            if (update_response.length == 32) {
                seq = Arrays.copyOfRange(update_response, 29, 32);
            }
            mGPCtx.setSessionKeys(keys.getSessionKeys(3, diversification_data, host_challenge, card_challenge));
        } else {
            throw new GPException("Don't know how to handle SCP version " + scpMajorVersion);
        }

        // Verify card cryptogram
        byte[] my_card_cryptogram = null;
        byte[] cntx = GPUtils.concatenate(host_challenge, card_challenge);
        if (scpMajorVersion == 1 || scpMajorVersion == 2) {
            my_card_cryptogram = GPCrypto.mac_3des_nulliv(mGPCtx.getSessionKeys().getKey(GPData.KeyType.ENC), cntx);
        } else {
            my_card_cryptogram = GPCrypto.scp03_kdf(mGPCtx.getSessionKeys().getKey(GPData.KeyType.MAC), (byte) 0x00, cntx, 64);
        }

        // This is the main check for possible successful authentication.
        if (!Arrays.equals(card_cryptogram, my_card_cryptogram)) {
            giveStrictWarning("Card cryptogram invalid!\nCard: " + HexUtils.bin2hex(card_cryptogram) + "\nHost: " + HexUtils.bin2hex(my_card_cryptogram) + "\n!!! DO NOT RE-TRY THE SAME COMMAND/KEYS OR YOU MAY BRICK YOUR CARD !!!");
        } else {
            logger.debug("Verified card cryptogram: " + HexUtils.bin2hex(my_card_cryptogram));
        }

        // Calculate host cryptogram and initialize SCP wrapper
        byte[] host_cryptogram = null;
        if (scpMajorVersion == 1 || scpMajorVersion == 2) {
            host_cryptogram = GPCrypto.mac_3des_nulliv(mGPCtx.getSessionKeys().getKey(GPData.KeyType.ENC), GPUtils.concatenate(card_challenge, host_challenge));
            mGPCtx.setWrapper(new GlobalPlatform.SCP0102Wrapper(mGPCtx.getSessionKeys(), scpVersion, EnumSet.of(GlobalPlatform.APDUMode.MAC), null, null, mGPCtx.getBlockSize()));
        } else {
            host_cryptogram = GPCrypto.scp03_kdf(mGPCtx.getSessionKeys().getKey(GPData.KeyType.MAC), (byte) 0x01, cntx, 64);
            mGPCtx.setWrapper(new GlobalPlatform.SCP03Wrapper(mGPCtx.getSessionKeys(), scpVersion, EnumSet.of(GlobalPlatform.APDUMode.MAC), null, null, mGPCtx.getBlockSize()));
        }

        logger.debug("Calculated host cryptogram: " + HexUtils.bin2hex(host_cryptogram));
        int P1 = GlobalPlatform.APDUMode.getSetValue(securityLevel);
        CommandAPDU externalAuthenticate = new CommandAPDU(CLA_MAC, ISO7816.INS_EXTERNAL_AUTHENTICATE_82, P1, 0, host_cryptogram);
        return wrap(externalAuthenticate);
    }


    @Override
    public Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "External authenticate failed");
        GlobalPlatform.SCPWrapper wrapper = mGPCtx.getWrapper();
        EnumSet<GlobalPlatform.APDUMode> securityLevel = mStashVar.getSecurityLevel();
        wrapper.setSecurityLevel(securityLevel);

        // FIXME: ugly stuff, ugly...
        if (mGPCtx.getScpMajorVersion() != 3) {
            GlobalPlatform.SCP0102Wrapper w = (GlobalPlatform.SCP0102Wrapper) wrapper;
            if (securityLevel.contains(GlobalPlatform.APDUMode.RMAC)) {
                w.setRMACIV(w.getIV());
            }
        }
        return 0;
    }
}
