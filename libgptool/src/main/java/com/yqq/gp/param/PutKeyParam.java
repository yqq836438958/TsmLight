package com.yqq.gp.param;

import java.util.List;

import pro.javacard.gp.GPKeySet;

public class PutKeyParam {
    private List<GPKeySet.GPKey> keys;
    private boolean replace;

    public List<GPKeySet.GPKey> getKeys() {
        return keys;
    }

    public void setKeys(List<GPKeySet.GPKey> keys) {
        this.keys = keys;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}
