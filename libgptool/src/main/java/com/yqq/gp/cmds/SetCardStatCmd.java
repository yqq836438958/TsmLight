package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;

public class SetCardStatCmd extends AbsGPCmd<Byte, Integer> {
    @Override
    protected CommandAPDU onReq(Byte b) throws CardException, GPException {
        CommandAPDU cmd = new CommandAPDU(CLA_GP, INS_SET_STATUS, 0x80, b);
        return wrap(cmd);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "SetCardStatCmd failed");
        return 0;
    }
}
