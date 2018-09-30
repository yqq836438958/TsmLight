package com.yqq.tsm;

import com.yqq.gp.IGPCmd;
import com.yqq.tsm.param.ApduCmd;
import com.yqq.tsm.param.ApduStep;
import com.yqq.tsm.param.TsmIn;
import com.yqq.tsm.param.TsmOut;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/12.
 */
// 设计初衷需要保持其原子性，一次性完成所有命令,相当于命令组，串行取命令
public abstract class TsmStep {
    private List<IGPCmd> cmdLists = null;
    private boolean autoSwitchNext = false;//自动执行下个Step
    protected TsmContext mTsmCtx = null;

    protected TsmStep() {
        cmdLists = new ArrayList<>();
        // TsmContext setValue ???
    }

    protected List<IGPCmd> getCmdLists() {
        return cmdLists;
    }

    protected void addCmd(IGPCmd cmd) {
        cmdLists.add(cmd);
    }

    // 处理返回数据
    public void postHandle(ApduStep src) throws CardException, GPException {
        List<ApduCmd> apduCmdList = src.getListApdus();
        int cmdIdx = 0;
        for (ApduCmd _apdu : apduCmdList) {
            if (src.isFeedback()) {
                //需要处理返回数据
                if (cmdIdx <= cmdLists.size()) {
                    cmdLists.get(cmdIdx).rsp(_apdu.getResponse());
                }
            } else {
                GPException.check(_apdu.getResponse(), "Install for Load failed");
            }
        }
    }

    // 请求数据
    ApduStep handle(TsmIn tsmIn) throws CardException, GPException {
        ApduStep tarApduStep = new ApduStep();
        if (!onSelfHandle(tarApduStep, tsmIn)) {
            ApduCmd tmpCmd = null;
            for (IGPCmd cmd : cmdLists) {
                tmpCmd = new ApduCmd(cmd.req(tsmIn));
                tarApduStep.appendCmd(tmpCmd);
            }
        }
        return tarApduStep;
    }

    protected boolean onSelfHandle(ApduStep tarApduStep, TsmIn tsmIn) throws CardException, GPException {
        return false;
    }
}
