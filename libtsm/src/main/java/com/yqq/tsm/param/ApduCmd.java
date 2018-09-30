package com.yqq.tsm.param;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class ApduCmd {
    private byte[] apdu;
    private String checker;
    private int index;
    private byte[] result;

    public ApduCmd(CommandAPDU _apdu) {
        apdu = _apdu.getData();
    }

    public ApduCmd(CommandAPDU _apdu, int _idx) {
        apdu = _apdu.getData();
        index = _idx;
    }

    public byte[] getApdu() {
        return apdu;
    }

    public void setApdu(byte[] apdu) {
        this.apdu = apdu;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public ResponseAPDU getResponse(){
        return new ResponseAPDU(result);
    }
}
