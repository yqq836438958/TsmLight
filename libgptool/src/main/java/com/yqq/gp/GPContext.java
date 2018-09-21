package com.yqq.gp;

import pro.javacard.gp.AID;
import pro.javacard.gp.GPKeySet;
import pro.javacard.gp.GlobalPlatform;

/**
 * Created by p_qingyuan on 2018/9/11.
 */
//代表就是一次会话类似于session
public class GPContext {
    private GPKeySet sessionKeys;
    private GlobalPlatform.SCPWrapper wrapper = null;
    private AID curSDAID;
    private int blockSize = 255;
    private int scpMajorVersion;

    public AID getCurSDAID() {
        return curSDAID;
    }

    public void setCurSDAID(AID sCurSDAID) {
        this.curSDAID = sCurSDAID;
    }

    public int getScpMajorVersion() {
        return scpMajorVersion;
    }

    public void setScpMajorVersion(int scpMajorVersion) {
        this.scpMajorVersion = scpMajorVersion;
    }


    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }


    public GlobalPlatform.SCPWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(GlobalPlatform.SCPWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public GPKeySet getSessionKeys() {
        return sessionKeys;
    }

    public void setSessionKeys(GPKeySet sessionKeys) {
        this.sessionKeys = sessionKeys;
    }

}
