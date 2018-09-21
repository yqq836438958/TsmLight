package com.yqq.tsm.param;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class ApduStep {
    public static final int STAT_INIT = -1;
    public static final int STAT_SUC = 0;
    public static final int STAT_FAIL = 1;
    public static final int STAT_PROGRESS = 2;
    private int step;
    private int nextStep;
    private List<ApduCmd> listApdus;
    private boolean feedback;
    private int status;
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<ApduCmd> getListApdus() {
        return listApdus;
    }

    public void setListApdus(List<ApduCmd> listApdus) {
        this.listApdus = listApdus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }
}
