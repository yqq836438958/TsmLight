package com.yqq.gp.cmds;

import com.google.common.collect.Lists;
import com.yqq.gp.AbsGPCmd;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERTags;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;

import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import apdu4j.HexUtils;
import apdu4j.ISO7816;
import pro.javacard.gp.AID;
import pro.javacard.gp.GPData;
import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class SelectCmd extends AbsGPCmd<AID> {

    @Override
    protected CommandAPDU onReq(AID sdAID) throws CardException, GPException {
        final CommandAPDU command;
        if (sdAID == null) {
            command = new CommandAPDU(ISO7816.CLA_ISO7816, ISO7816.INS_SELECT, 0x04, 0x00, 256);
        } else {
            command = new CommandAPDU(ISO7816.CLA_ISO7816, ISO7816.INS_SELECT, 0x04, 0x00, sdAID.getBytes(), 256);
        }
        return command;
    }

    @Override
    public ResponseAPDU onRsp(ResponseAPDU resp) throws CardException, GPException {
        AID sdAID = mStashVar;
        if (sdAID == null && resp.getSW() == 0x6A82) {
            // If it has the identification AID, it probably is an unfused JCOP
            byte[] identify_aid = HexUtils.hex2bin("A000000167413000FF");
            CommandAPDU identify = new CommandAPDU(ISO7816.CLA_ISO7816, ISO7816.INS_SELECT, 0x04, 0x00, identify_aid, 256);
            ResponseAPDU identify_resp = channel.transmit(identify);
            byte[] identify_data = identify_resp.getData();
            // Check the fuse state
            if (identify_data.length > 15) {
                if (identify_data[14] == 0x00) {
                    giveStrictWarning("Unfused JCOP detected");
                }
            }
        }
        // If the ISD is locked, log it.
        if (resp.getSW() == 0x6283) {
            logger.warn("SELECT ISD returned 6283 - CARD_LOCKED");
        }

        if (resp.getSW() == 0x9000 || resp.getSW() == 0x6283) {
            // The security domain AID is in FCI.
            byte[] fci = resp.getData();
            parse_select_response(fci);
        }
        return resp;
    }

    private void parse_select_response(byte[] fci) throws GPException {
        try (ASN1InputStream ais = new ASN1InputStream(fci)) {
            if (ais.available() > 0) {
                // Read FCI
                DERApplicationSpecific fcidata = (DERApplicationSpecific) ais.readObject();
                // FIXME System.out.println(ASN1Dump.dumpAsString(fcidata, true));
                if (fcidata.getApplicationTag() == 15) {
                    ASN1Sequence s = ASN1Sequence.getInstance(fcidata.getObject(BERTags.SEQUENCE));
                    for (ASN1Encodable e : Lists.newArrayList(s.iterator())) {
                        ASN1TaggedObject t = DERTaggedObject.getInstance(e);
                        if (t.getTagNo() == 4) {
                            // ISD AID
                            ASN1OctetString isdaid = DEROctetString.getInstance(t.getObject());
                            AID detectedAID = new AID(isdaid.getOctets());
                            if (sdAID == null) {
                                logger.debug("Auto-detected ISD AID: " + detectedAID);
                            }
                            if (sdAID != null && !detectedAID.equals(sdAID)) {
                                giveStrictWarning("SD AID in FCI does not match the requested AID!");
                            }
                            this.sdAID = sdAID == null ? detectedAID : sdAID;
                        } else if (t.getTagNo() == 5) {
                            // Proprietary, usually a sequence
                            if (t.getObject() instanceof ASN1Sequence) {
                                ASN1Sequence prop = ASN1Sequence.getInstance(t.getObject());
                                for (ASN1Encodable enc : Lists.newArrayList(prop.iterator())) {
                                    ASN1Primitive proptag = enc.toASN1Primitive();
                                    if (proptag instanceof DERApplicationSpecific) {
                                        DERApplicationSpecific isddata = (DERApplicationSpecific) proptag;
                                        if (isddata.getApplicationTag() == 19) {
                                            spec = GPData.get_version_from_card_data(isddata.getEncoded());
                                            logger.debug("Auto-detected GP version: " + spec);
                                        }
                                    } else if (proptag instanceof DERTaggedObject) {
                                        DERTaggedObject tag = (DERTaggedObject) proptag;
                                        if (tag.getTagNo() == 101) {
                                            setBlockSize(DEROctetString.getInstance(tag.getObject()));
                                        } else if (tag.getTagNo() == 110) {
                                            logger.debug("Lifecycle data (ignored): " + HexUtils.bin2hex(tag.getObject().getEncoded()));
                                        } else {
                                            logger.info("Unknown/unhandled tag in FCI proprietary data: " + HexUtils.bin2hex(tag.getEncoded()));
                                        }
                                    } else {
                                        throw new GPException("Unknown data from card: " + HexUtils.bin2hex(proptag.getEncoded()));
                                    }
                                }
                            } else {
                                // Except Feitian cards which have a plain nested tag
                                if (t.getObject() instanceof DERTaggedObject) {
                                    DERTaggedObject tag = (DERTaggedObject) t.getObject();
                                    if (tag.getTagNo() == 101) {
                                        setBlockSize(DEROctetString.getInstance(tag.getObject()));
                                    } else {
                                        logger.info("Unknown/unhandled tag in FCI proprietary data: " + HexUtils.bin2hex(tag.getEncoded()));
                                    }
                                }
                            }
                        } else {
                            logger.info("Unknown/unhandled tag in FCI: " + HexUtils.bin2hex(t.getEncoded()));
                        }
                    }
                } else {
                    throw new GPException("Unknown data from card: " + HexUtils.bin2hex(fci));
                }
            }
        } catch (IOException | ClassCastException e) {
            throw new GPException("Invalid data: " + e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
