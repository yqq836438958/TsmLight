package com.yqq.gp.param;

import java.util.EnumSet;

import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.GlobalPlatform;
import pro.javacard.gp.SessionKeyProvider;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class SessionParam {
    SessionKeyProvider keys;
    byte[] host_challenge;
    EnumSet<GlobalPlatform.APDUMode> securityLevel;
    int scpVersion;
    ResponseAPDU tmpAPDU;

    public SessionKeyProvider getKeys() {
        return keys;
    }

    public void setKeys(SessionKeyProvider keys) {
        this.keys = keys;
    }

    public byte[] getHost_challenge() {
        return host_challenge;
    }

    public void setHost_challenge(byte[] host_challenge) {
        this.host_challenge = host_challenge;
    }

    public EnumSet<GlobalPlatform.APDUMode> getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(EnumSet<GlobalPlatform.APDUMode> securityLevel) {
        this.securityLevel = securityLevel;
    }

    public int getScpVersion() {
        return scpVersion;
    }

    public void setScpVersion(int scpVersion) {
        this.scpVersion = scpVersion;
    }

    public ResponseAPDU getTmpAPDU() {
        return tmpAPDU;
    }

    public void setTmpAPDU(ResponseAPDU tmpAPDU) {
        this.tmpAPDU = tmpAPDU;
    }
}
