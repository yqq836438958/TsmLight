package com.yqq.tsm;

import com.yqq.gp.IGPCmd;
import com.yqq.tsm.param.TsmCommand;
import com.yqq.tsm.param.TsmResponse;

import java.util.List;

import javax.smartcardio.CardException;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/12.
 */
// 设计初衷需要保持其原子性，一次性完成所有命令,相当于命令组，串行取命令
public abstract class TSMStep {
    protected List<IGPCmd> cmdLists = null;
    protected int cmdCursor = 0; // 用于记录当前cmd的序列

    TsmCommand process(TsmResponse repsonse) throws CardException, GPException {
        TsmCommand apdu = onProcess(repsonse);
        return apdu;
    }

    protected abstract TsmCommand onProcess(TsmResponse response) throws CardException, GPException;

    protected void moveCusor(){
        cmdCursor++;
        if(cmdCursor >= cmdLists.size()){

        }
    }
}
