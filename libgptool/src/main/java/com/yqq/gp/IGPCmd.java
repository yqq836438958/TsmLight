package com.yqq.gp;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public interface IGPCmd<Param, Ret> {
    public CommandAPDU req(Param param) throws CardException, GPException;

    public Ret rsp(ResponseAPDU rsp) throws CardException, GPException;
}
