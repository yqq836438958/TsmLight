package com.yqq.tsm;

import com.yqq.gp.IGPCmd;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TsmTask {
    private String session;
    private List<TsmStep> listSteps = null;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public List<TsmStep> getListSteps() {
        return listSteps;
    }

    public void setListSteps(List<TsmStep> listSteps) {
        this.listSteps = listSteps;
    }

    public TsmStep getCmdByStep(int _step) {
        if (listSteps == null || _step >= listSteps.size()) {
            return null;
        }
        return listSteps.get(_step);
    }
}
