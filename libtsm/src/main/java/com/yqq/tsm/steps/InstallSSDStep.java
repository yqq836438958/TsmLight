package com.yqq.tsm.steps;

import com.yqq.tsm.TSMStep;
import com.yqq.tsm.param.ApduData;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class InstallSSDStep extends TSMStep {
    @Override
    protected ApduData onProcessReq(int cmdIndex, byte[] param) {
        return null;
    }

    @Override
    protected ApduData onProcessRsp(int cmdIndex, byte[] param) {
        return null;
    }
}
