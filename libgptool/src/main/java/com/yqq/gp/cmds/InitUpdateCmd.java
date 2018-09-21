package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.SessionParam;

import java.security.SecureRandom;
import java.util.EnumSet;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import apdu4j.ISO7816;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GlobalPlatform;
import pro.javacard.gp.SessionKeyProvider;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class InitUpdateCmd extends AbsGPCmd<SessionParam, ResponseAPDU> {

    @Override
    public CommandAPDU onReq(SessionParam sessionParam) throws CardException, GPException {
        SessionKeyProvider keys = sessionParam.getKeys();
        byte[] host_challenge = sessionParam.getHost_challenge();
        EnumSet<GlobalPlatform.APDUMode> securityLevel = sessionParam.getSecurityLevel();
        if (securityLevel.contains(GlobalPlatform.APDUMode.ENC)) {
            securityLevel.add(GlobalPlatform.APDUMode.MAC);
        }
        // Generate host challenge
        if (host_challenge == null) {
            host_challenge = new byte[8];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(host_challenge);
        }

        // P1 key version (SCP1)
        // P2 either key ID (SCP01) or 0 (SCP2)
        // TODO: use it here for KeyID?
        CommandAPDU initUpdate = new CommandAPDU(CLA_GP, INS_INITIALIZE_UPDATE, keys.getKeysetVersion(), keys.getKeysetID(), host_challenge, 256);
        return initUpdate;
    }

    @Override
    public ResponseAPDU onRsp(ResponseAPDU rsp) throws CardException, GPException {
        int sw = rsp.getSW();
        // Detect and report locked cards in a more sensible way.
        if ((sw == ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED) || (sw == ISO7816.SW_AUTHENTICATION_METHOD_BLOCKED)) {
            throw new GPException(sw, "INITIALIZE UPDATE failed, card LOCKED?");
        }
        // Detect all other errors
        GPException.check(rsp, "INITIALIZE UPDATE failed");
        return rsp;
    }
}
