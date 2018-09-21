package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import apdu4j.ISO7816;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GPUtils;

public class GetCplcCmd extends AbsGPCmd<Void, byte[]> {
    @Override
    protected CommandAPDU onReq(Void aVoid) throws CardException, GPException {
        CommandAPDU command = new CommandAPDU(CLA_GP, INS_GET_DATA, 0x9F, 0x7F, 256);
        return command;
    }

    @Override
    protected byte[] onRsp(ResponseAPDU rsp) throws CardException, GPException {
        if (rsp.getSW() == ISO7816.SW_NO_ERROR) {
            return rsp.getData();
        } else {
            logger.debug("GET DATA(CPLC) returned SW: " + GPUtils.swToString(rsp.getSW()));
        }
        return null;
    }
}
