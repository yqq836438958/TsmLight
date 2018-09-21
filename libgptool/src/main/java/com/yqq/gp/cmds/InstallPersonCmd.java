package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.StoreDataParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.AID;
import pro.javacard.gp.GPException;

public class InstallPersonCmd extends AbsGPCmd<StoreDataParam, Integer> {
    @Override
    protected CommandAPDU onReq(StoreDataParam _param) throws CardException, GPException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        AID aid = _param.getAid();
        try {
            // GP 2.1.1 9.5.2.3.5
            bo.write(0);
            bo.write(0);
            bo.write(aid.getLength());
            bo.write(aid.getBytes());
            bo.write(0);
            bo.write(0);
            bo.write(0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        CommandAPDU install = new CommandAPDU(CLA_GP, INS_INSTALL, 0x20, 0x00, bo.toByteArray());
        return wrap(install);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "Install for Load failed");
        return 0;
    }
}
