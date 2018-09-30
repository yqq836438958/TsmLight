package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import apdu4j.ISO7816;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GPUtils;

public class GetDataCmd extends AbsGPCmd<Void, byte[]> {
    @Override
    protected CommandAPDU onReq(Void aVoid) throws CardException, GPException {
        CommandAPDU apdu = new CommandAPDU(CLA_GP, ISO7816.INS_GET_DATA, 0x00, 0x66, 256);
        return wrap(apdu);
    }

    @Override
    protected byte[] onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        if (response.getSW() == 0x6A86) {
            logger.debug("GET DATA(CardData) not supported, Open Platform 2.0.1 card? " + GPUtils.swToString(resp.getSW()));
            return null;
        } else if (response.getSW() == 0x9000) {
            return response.getData();
        }
        return null;
    }
}
