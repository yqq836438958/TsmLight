package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.DeleteAidParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.AID;
import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class DeleteAidCmd extends AbsGPCmd<DeleteAidParam, Integer> {
    @Override
    protected CommandAPDU onReq(DeleteAidParam deleteAidParam) throws CardException, GPException {
        AID aid = deleteAidParam.getAid();
        boolean deleteDeps = deleteAidParam.isDeleteDeps();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            bo.write(0x4f);
            bo.write(aid.getLength());
            bo.write(aid.getBytes());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        CommandAPDU delete = new CommandAPDU(CLA_GP, INS_DELETE, 0x00, deleteDeps ? 0x80 : 0x00, bo.toByteArray());
        return wrap(delete);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "Deletion failed");
        return 0;
    }
}
