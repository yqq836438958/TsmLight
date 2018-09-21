package com.yqq.tsm;

import com.yqq.tsm.param.ApduStep;
import com.yqq.tsm.param.TsmCommand;
import com.yqq.tsm.param.TsmResponse;
import com.yqq.tsm.steps.InstallSSDStep;
import com.yqq.tsm.steps.OpenSessionStep;

import java.util.List;

import javax.smartcardio.CardException;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TSMService {
    // 基于http非socket方式，所以，都必须由客户端进行发起
    // 开卡
    public TsmCommand cardIssue(byte[] param) {
        TSMTask session = new TSMTask();
        session.addStep(new OpenSessionStep());// 打开会话
        session.addStep(new InstallSSDStep());
//        return session.getCmdByStep(0).processReq(0, param);
        return null;
    }

    // 充值
    public TsmCommand cardTopup(byte[] param) {
        return null;
    }

    // 设备端返回结果数据过来，服务端解析再传递过去
    public TsmCommand apduTransmit(TsmResponse apdus) throws CardException, GPException {
        String sessKey = apdus.getSession();
        TSMTask session = TSMTaskMgr.getInstance().findSessionBySessionKey(sessKey);
        TSMStep step = findCurStep(session, apdus);
        if (step != null) {
            return step.process(apdus);
        }
        return null;
    }

    private TSMStep findCurStep(TSMTask task, TsmResponse apdus) {
        List<ApduStep> listSteps = apdus.getListSteps();
        TSMStep tarStep = null;
        int stat = 0;
        boolean errHappen = false;
        for (ApduStep step : listSteps) {
            if(step.getStep() == ApduStep.STAT_INIT){
                break;
            }
            tarStep = processSingleStepStatus(task,step);
            if(tarStep != null){
                break;
            }
        }
        if (!errHappen && tarStep == null) {
            int iLastStep = listSteps.get(listSteps.size() -1).getStep();
            tarStep = task.getCmdByStep(iLastStep+1);
        }
        return tarStep;
    }

    private TSMStep processSingleStepStatus(TSMTask session, ApduStep step) {
        TSMStep tarStep = null;
        int stat = step.getStatus();
        switch (stat) {
            case ApduStep.STAT_FAIL:
            case ApduStep.STAT_PROGRESS:
                tarStep = session.getCmdByStep(step.getStep());
                break;
        }
        return tarStep;
    }
}
