package com.yqq.tsm.steps;

import com.yqq.gp.cmds.InitUpdateCmd;
import com.yqq.gp.param.SessionParam;
import com.yqq.tsm.TsmStep;
import com.yqq.tsm.param.ApduCmd;
import com.yqq.tsm.param.ApduStep;
import com.yqq.tsm.param.TsmIn;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;

import pro.javacard.gp.GPException;

public class InitUpdateStep extends TsmStep {
    public InitUpdateStep() {
        super();
        addCmd(new InitUpdateCmd());
    }

    @Override
    protected boolean onSelfHandle(ApduStep tarApduStep, TsmIn tsmIn) throws CardException, GPException {
        InitUpdateCmd initUpdateCmd = (InitUpdateCmd) getCmdLists().get(0);
        SessionParam sessionParam = new SessionParam();
        sessionParam.setKeys(null);
        sessionParam.setScpVersion(0);
//        sessionParam.setSecurityLevel();
        CommandAPDU apdu = initUpdateCmd.req(sessionParam);
        tarApduStep.appendCmd(new ApduCmd(apdu));
        return false;
    }
}
