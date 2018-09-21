package com.yqq.gp.param;

import pro.javacard.gp.AID;
import pro.javacard.gp.GPRegistryEntry;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class InstallParam {
    AID packageAID;
    AID appletAID;
    AID instanceAID;
    GPRegistryEntry.Privileges privileges;
    byte[] installParams;
    byte[] installToken;

    public AID getPackageAID() {
        return packageAID;
    }

    public void setPackageAID(AID packageAID) {
        this.packageAID = packageAID;
    }

    public AID getAppletAID() {
        return appletAID;
    }

    public void setAppletAID(AID appletAID) {
        this.appletAID = appletAID;
    }

    public AID getInstanceAID() {
        return instanceAID;
    }

    public void setInstanceAID(AID instanceAID) {
        this.instanceAID = instanceAID;
    }

    public GPRegistryEntry.Privileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(GPRegistryEntry.Privileges privileges) {
        this.privileges = privileges;
    }

    public byte[] getInstallParams() {
        return installParams;
    }

    public void setInstallParams(byte[] installParams) {
        this.installParams = installParams;
    }

    public byte[] getInstallToken() {
        return installToken;
    }

    public void setInstallToken(byte[] installToken) {
        this.installToken = installToken;
    }
}
