package com.yqq.tsm.steps;

import com.yqq.tsm.TSMStep;
import com.yqq.tsm.param.ApduData;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class InstallLoadStep extends TSMStep {
    public InstallLoadStep(){
//        cmdLists.add();
//        cmdLists.add();
    }
    @Override
    protected ApduData onProcessReq(int cmdIndex, byte[] param) {
        //TODO for-reach

        return null;
    }

    @Override
    protected ApduData onProcessRsp(int cmdIndex, byte[] param) {
        return null;
    }
}
