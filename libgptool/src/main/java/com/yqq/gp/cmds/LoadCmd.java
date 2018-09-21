package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.LoadParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.CapFile;
import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class LoadCmd extends AbsGPCmd<LoadParam, Integer> {
    @Override
    protected CommandAPDU onReq(LoadParam _loadParam) throws CardException, GPException {
        CapFile cap = _loadParam.getCap();
        boolean includeDebug = _loadParam.isIncludeDebug();
        boolean separateComponents = _loadParam.isSeparateComponents();
        // FIXME: parameters are optional for load
        List<byte[]> blocks = cap.getLoadBlocks(includeDebug, separateComponents, mGPCtx.getWrapper().getBlockSize());
        int blockIndex = _loadParam.getCapBlockIndex();
        CommandAPDU load = new CommandAPDU(CLA_GP, INS_LOAD, (blockIndex == (blocks.size() - 1)) ? 0x80 : 0x00, (byte) blockIndex, blocks.get(blockIndex));
        return wrap(load);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "Install for Load failed");
        return 0;
    }
}
