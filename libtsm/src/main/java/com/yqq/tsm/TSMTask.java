package com.yqq.tsm;

import com.yqq.gp.IGPCmd;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TSMTask {
    private String session;
    private List<TSMStep> listSteps = null;

    public TSMStep getCmdByStep(int step) {
        return listSteps.get(step);
    }

    void addStep(TSMStep step) {
        listSteps.add(step);
    }
}
