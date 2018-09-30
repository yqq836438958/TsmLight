package com.yqq.tsm;

import com.yqq.tsm.param.ApduStep;
import com.yqq.tsm.param.TsmIn;
import com.yqq.tsm.param.TsmOut;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.smartcardio.CardException;

import pro.javacard.gp.GPException;

// 注册及派发 step 的总线
public class TsmBus {
    private static TsmBus instance = null;
    private ConcurrentHashMap<String, TsmTask> mTaskMap = new ConcurrentHashMap<String, TsmTask>();

    public static TsmBus getInstance() {
        return instance;
    }

    public void regist(List<TsmStep> steps) {
        TsmTask task = new TsmTask();
        task.setListSteps(steps);
        String sessionKey = System.currentTimeMillis() + "";
        task.setSession(sessionKey);
        mTaskMap.put(sessionKey, task);
    }

    public void post(TsmIn in, TsmOut out) {
        postInternal(in, out);
    }

    private void postInternal(TsmIn in, TsmOut out){
        TsmTask session = mTaskMap.get(in.getSession());
        if (session != null) {
            try {
                handleTsmIn(session, in, out);
            } catch (CardException e) {
                e.printStackTrace();
                out.setErrcode(-1); //TODO
            } catch (GPException e) {
                e.printStackTrace();
                out.setErrcode(-2);// TODO
            }
        }
    }

    private void handleTsmIn(TsmTask task, TsmIn _in, TsmOut _out) throws CardException, GPException {
        List<ApduStep> listSteps = _in.getListSteps();
        boolean needMoreStep = false;
        for (ApduStep _step : listSteps) {
            needMoreStep = (_step.getStep() == ApduStep.STAT_SUC);
            if (_step.getStep() == ApduStep.STAT_INIT) {
                handleNextStep(task, _step.getStep() + 1, _in, _out);
                break;
            }
            handleCurStep(task, _step);
            if (_step.getStep() == ApduStep.STAT_FAIL) {
                break;
            }
        }
        if (needMoreStep) {
            handleNextStep(task, listSteps.get(listSteps.size() - 1).getStep() + 1, _in, _out);
        }
    }

    private void handleCurStep(TsmTask task, ApduStep apduStep) throws CardException, GPException {
        TsmStep tmpStep = task.getCmdByStep(apduStep.getStep());
        if (tmpStep != null) {
            tmpStep.postHandle(apduStep);
        }
    }

    private void handleNextStep(TsmTask task, int stepIndex, TsmIn _in, TsmOut _out) throws CardException, GPException {
        TsmStep tmpStep = task.getCmdByStep(stepIndex);
        if (tmpStep != null) {
            ApduStep apduStep =  tmpStep.handle(_in);
            if(apduStep != null) {
                _out.appendApduStep(apduStep, false);
            }
        }
    }
}
