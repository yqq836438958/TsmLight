package com.yqq.tsm.param;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class TsmResponse {
    private int errcode;//0 代表有后续命令，1代表已经finish,小于0代表异常
    private List<ApduStep> listSteps;
    private String session;
    private String token;//keep

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

}
