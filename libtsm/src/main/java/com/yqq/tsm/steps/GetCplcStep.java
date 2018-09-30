package com.yqq.tsm.steps;

import com.yqq.gp.IGPCmd;
import com.yqq.gp.cmds.GetCplcCmd;
import com.yqq.tsm.TsmStep;
import com.yqq.tsm.param.ApduCmd;
import com.yqq.tsm.param.ApduStep;

import java.util.List;

import javax.smartcardio.CardException;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class GetCplcStep extends TsmStep{
    public GetCplcStep(){
        super();
        addCmd(new GetCplcCmd());
    }
    @Override
    public void postHandle(ApduStep src) throws CardException, GPException {
        GetCplcCmd cmd = (GetCplcCmd) getCmdLists().get(0);
        List<ApduCmd> cmdList = src.getListApdus();
        if(cmdList != null && cmdList.size() > 0){
            byte[] cplc = cmd.rsp(cmdList.get(0).getResponse());
            mTsmCtx.saveCplc(cplc);
        }
    }
}
