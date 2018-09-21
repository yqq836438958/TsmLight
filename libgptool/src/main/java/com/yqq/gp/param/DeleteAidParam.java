package com.yqq.gp.param;

import pro.javacard.gp.AID;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class DeleteAidParam {
    AID aid;
    boolean deleteDeps;

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public boolean isDeleteDeps() {
        return deleteDeps;
    }

    public void setDeleteDeps(boolean deleteDeps) {
        this.deleteDeps = deleteDeps;
    }
}
