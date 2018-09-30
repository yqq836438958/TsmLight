package com.yqq.tsm.steps;

import com.yqq.tsm.TsmStep;
import com.yqq.tsm.param.ApduStep;
import com.yqq.tsm.param.TsmIn;

import javax.smartcardio.CardException;

import pro.javacard.gp.GPException;

public class InstallSSDStep extends TsmStep {
    @Override
    protected boolean onSelfHandle(ApduStep tarApduStep, TsmIn tsmIn) throws CardException, GPException {
        return true;
    }
}
