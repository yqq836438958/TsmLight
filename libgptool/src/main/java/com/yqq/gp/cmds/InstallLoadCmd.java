package com.yqq.gp.cmds;

import com.yqq.gp.AbsGPCmd;
import com.yqq.gp.param.LoadParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import pro.javacard.gp.CapFile;
import pro.javacard.gp.GPException;

/**
 * Created by p_qingyuan on 2018/9/13.
 */

public class InstallLoadCmd extends AbsGPCmd<LoadParam, Integer> {
    @Override
    protected CommandAPDU onReq(LoadParam _loadParam) throws CardException, GPException {
        CapFile cap = _loadParam.getCap();
        boolean includeDebug = _loadParam.isIncludeDebug();
        boolean separateComponents = _loadParam.isSeparateComponents();
        boolean loadParam = _loadParam.isLoadParam();
        boolean useHash = _loadParam.isUseHash();
        byte[] hash = useHash ? cap.getLoadFileDataHash("SHA1", includeDebug) : new byte[0];
        int len = cap.getCodeLength(includeDebug);
        // FIXME: parameters are optional for load
        byte[] loadParams = loadParam ? new byte[]{(byte) 0xEF, 0x04, (byte) 0xC6, 0x02, (byte) ((len & 0xFF00) >> 8),
                (byte) (len & 0xFF)} : new byte[0];

        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        try {
            bo.write(cap.getPackageAID().getLength());
            bo.write(cap.getPackageAID().getBytes());

            bo.write(mGPCtx.getCurSDAID().getLength());
            bo.write(mGPCtx.getCurSDAID().getBytes());

            bo.write(hash.length);
            bo.write(hash);

            bo.write(loadParams.length);
            bo.write(loadParams);
            bo.write(0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        CommandAPDU installForLoad = new CommandAPDU(CLA_GP, INS_INSTALL, 0x02, 0x00, bo.toByteArray());
        return wrap(installForLoad);
    }

    @Override
    protected Integer onRsp(ResponseAPDU rsp) throws CardException, GPException {
        ResponseAPDU response = unwrap(rsp);
        GPException.check(response, "Install for Load failed");
        return 0;
    }
}