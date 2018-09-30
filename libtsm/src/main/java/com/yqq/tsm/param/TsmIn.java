package com.yqq.tsm.param;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TsmIn {
    private int errcode;//0 代表有后续命令，1代表已经finish,小于0代表异常
    private List<ApduStep> listSteps;
    private String session;
    private String token;//keep
    private String msg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public List<ApduStep> getListSteps() {
        return listSteps;
    }

    public void setListSteps(List<ApduStep> listSteps) {
        this.listSteps = listSteps;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
