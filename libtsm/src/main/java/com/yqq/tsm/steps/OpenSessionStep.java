package com.yqq.tsm.steps;

import com.yqq.gp.IGPCmd;
import com.yqq.gp.cmds.ExternAuthCmd;
import com.yqq.gp.cmds.InitUpdateCmd;
import com.yqq.gp.param.SessionParam;
import com.yqq.tsm.TSMStep;
import com.yqq.tsm.param.ApduCmd;
import com.yqq.tsm.param.ApduData;

import java.util.ArrayList;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class OpenSessionStep extends TSMStep {
    public OpenSessionStep() {
        cmdLists = new ArrayList<>();
        cmdLists.add(new InitUpdateCmd());
        cmdLists.add(new ExternAuthCmd());
    }

    private TSMStep reqInitUpdate(ApduData src) throws CardException, GPException {
        int cmdIdx = 0; // TODO
        IGPCmd<SessionParam,ResponseAPDU> cmd = cmdLists.get(cmdIdx);
        CommandAPDU apdu = cmd.req(null);
        ApduCmd tar = new ApduCmd(apdu,cmdIdx);

        return new ApduData();
    }

    @Override
    protected ApduData onProcessReq(int cmdIndex, byte[] param) {
        IGPCmd cmd = cmdLists.get(cmdIndex);
        try {
            CommandAPDU apdu = cmd.req(param);
        } catch (CardException e) {
            e.printStackTrace();
        } catch (GPException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected ApduData onProcessRsp(int cmdIndex, byte[] param) {
        return null;
    }
}
