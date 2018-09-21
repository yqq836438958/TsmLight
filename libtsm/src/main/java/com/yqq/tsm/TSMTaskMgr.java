package com.yqq.tsm;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TSMTaskMgr {
    private List<TSMTask> sessions;
    private static TSMTaskMgr instance = null;

    public static TSMTaskMgr getInstance() {
        if (instance == null) {
            synchronized (TSMTaskMgr.class) {
                if (instance == null) {
                    instance = new TSMTaskMgr();
                }
            }
        }
        return instance;
    }

    void saveSession(TSMTask session) {

    }

    public TSMTask findSessionBySessionKey(String sessionKey) {
        return null;
    }
}
