package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.StoreDataParam;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;
import pro.javacard.gp.GPUtils;

public class StoreDataCmd extends AbsGPCmd<StoreDataParam, Integer> {

    @Override
    protected CommandAPDU onReq(StoreDataParam _param) throws CardException, GPException {
        List<byte[]> blocks = _param.getBlocks();
        if (blocks == null) {
            blocks = GPUtils.splitArray(_param.getOriginDat(), mGPCtx.getWrapper().getBlockSize());
            _param.setBlocks(blocks);
        }
        int i = _param.getCurBlockIndex();
        CommandAPDU storeAPdu = new CommandAPDU(CLA_GP, INS_STORE_DATA, (i == (blocks.size() - 1)) ? 0x80 : 0x00, (byte) i, blocks.get(i));
        _param.inCurBlockIndex();
        return wrap(storeAPdu);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "Store Data failed");
        return 0;
    }
}
