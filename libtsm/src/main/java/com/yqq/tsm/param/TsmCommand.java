package com.yqq.tsm.param;

import java.util.List;

/**
 * Created by p_qingyuan on 2018/9/12.
 */

public class TsmCommand {
    private int errcode;//0 代表有后续命令，1代表已经finish,小于0代表异常
    private List<ApduStep> listSteps;
    private String session;
    private String token;//keep
    private String msg;
}
