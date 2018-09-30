package com.yqq.gp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;
import pro.javacard.gp.GlobalPlatform;

/**
 * Created by p_qingyuan on 2018/9/11.
 */
// change to handle/ posthandle
public abstract class AbsGPCmd<Param, Ret> implements IGPCmd<Param, Ret>, GPConstants {
    protected static Logger logger = LoggerFactory.getLogger(AbsGPCmd.class);
    protected boolean strict = true;
    protected GPContext mGPCtx = null;
    protected Param mStashVar = null;

    public void setGPCtx(GPContext _ctx) {
        mGPCtx = _ctx;
    }

    protected void giveStrictWarning(String message) throws GPException {
        message = "STRICT WARNING: " + message;
        if (strict) {
            throw new GPException(message);
        } else {
            logger.warn(message);
        }
    }

    protected ResponseAPDU unwrap(ResponseAPDU rsp) throws GPException {
        GlobalPlatform.SCPWrapper wrapper = mGPCtx.getWrapper();
        if (wrapper != null) {
            return wrapper.unwrap(rsp);
        }
        return rsp;
    }

    protected CommandAPDU wrap(CommandAPDU apdu) throws GPException {
        GlobalPlatform.SCPWrapper wrapper = mGPCtx.getWrapper();
        if (wrapper != null) {
            return wrapper.wrap(apdu);
        }
        return apdu;
    }

    @Override
    public CommandAPDU req(Param param) throws CardException, GPException {
        mStashVar = param;
        return onReq(param);
    }

    @Override
    public Ret rsp(ResponseAPDU rsp) throws CardException, GPException {
        Ret responseAPDU = onRsp(rsp);
        mStashVar = null;
        return responseAPDU;
    }

    protected abstract CommandAPDU onReq(Param param) throws CardException, GPException;

    protected abstract Ret onRsp(ResponseAPDU rsp) throws CardException, GPException;
}
