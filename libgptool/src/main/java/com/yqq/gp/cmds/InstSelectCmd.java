package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.InstallParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.AID;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GPRegistryEntry;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public class InstSelectCmd extends AbsGPCmd<InstallParam, Integer> {

    @Override
    public CommandAPDU onReq(InstallParam param) throws CardException, GPException {
        AID packageAID = param.getPackageAID();
        AID appletAID = param.getAppletAID();
        AID instanceAID = param.getInstanceAID();
        GPRegistryEntry.Privileges privileges = param.getPrivileges();
        byte[] installParams = param.getInstallParams();
        byte[] installToken = param.getInstallToken();
        if (instanceAID == null) {
            instanceAID = appletAID;
        }
        //TODO 需要先做内容判断
//        if (getRegistry().allAppletAIDs().contains(instanceAID)) {
//            giveStrictWarning("Instance AID " + instanceAID + " is already present on card");
//        }
        if (installParams == null) {
            installParams = new byte[]{(byte) 0xC9, 0x00};
        }
        if (installToken == null) {
            installToken = new byte[0];
        }
        byte[] privs = privileges.toBytes();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            bo.write(packageAID.getLength());
            bo.write(packageAID.getBytes());

            bo.write(appletAID.getLength());
            bo.write(appletAID.getBytes());

            bo.write(instanceAID.getLength());
            bo.write(instanceAID.getBytes());

            bo.write(privs.length);
            bo.write(privs);

            bo.write(installParams.length);
            bo.write(installParams);

            bo.write(installToken.length);
            bo.write(installToken);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        CommandAPDU install = new CommandAPDU(CLA_GP, INS_INSTALL, 0x0C, 0x00, bo.toByteArray());
        return wrap(install);
    }

    @Override
    public Integer onRsp(ResponseAPDU _rsp) throws CardException, GPException {
        ResponseAPDU rsp = unwrap(_rsp);
        GPException.check(rsp, "Install for Install and make selectable failed");
        return 0;
    }
}
