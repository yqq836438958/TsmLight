package com.yqq.gp.param;

import pro.javacard.gp.CapFile;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class LoadParam {
    private CapFile cap;
    private boolean includeDebug;
    private boolean separateComponents;
    private boolean loadParam;
    private boolean useHash;
    private int capBlockIndex;

    public int getCapBlockIndex() {
        return capBlockIndex;
    }

    public void setCapBlockIndex(int capBlockIndex) {
        this.capBlockIndex = capBlockIndex;
    }

    public CapFile getCap() {
        return cap;
    }

    public void setCap(CapFile cap) {
        this.cap = cap;
    }

    public boolean isIncludeDebug() {
        return includeDebug;
    }

    public void setIncludeDebug(boolean includeDebug) {
        this.includeDebug = includeDebug;
    }

    public boolean isSeparateComponents() {
        return separateComponents;
    }

    public void setSeparateComponents(boolean separateComponents) {
        this.separateComponents = separateComponents;
    }

    public boolean isLoadParam() {
        return loadParam;
    }

    public void setLoadParam(boolean loadParam) {
        this.loadParam = loadParam;
    }

    public boolean isUseHash() {
        return useHash;
    }

    public void setUseHash(boolean useHash) {
        this.useHash = useHash;
    }
}
