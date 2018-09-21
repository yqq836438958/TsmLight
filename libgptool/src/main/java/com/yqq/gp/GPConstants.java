package com.yqq.gp;

/**
 * Created by p_qingyuan on 2018/9/11.
 */

public interface GPConstants {
    public static final byte CLA_GP = (byte) 0x80;
    public static final byte CLA_MAC = (byte) 0x84;
    public static final byte INS_INITIALIZE_UPDATE = (byte) 0x50;
    public static final byte INS_INSTALL = (byte) 0xE6;
    public static final byte INS_LOAD = (byte) 0xE8;
    public static final byte INS_DELETE = (byte) 0xE4;
    public static final byte INS_GET_STATUS = (byte) 0xF2;
    public static final byte INS_SET_STATUS = (byte) 0xF0;
    public static final byte INS_PUT_KEY = (byte) 0xD8;
    public static final byte INS_STORE_DATA = (byte) 0xE2;
    public static final byte INS_GET_DATA = (byte) 0xCA;
    public static final short SHORT_0 = 0;
    public static final int SCP_ANY = 0;
    public static final int SCP_01_05 = 1;
    public static final int SCP_01_15 = 2;
    public static final int SCP_02_04 = 3;
    public static final int SCP_02_05 = 4;
    public static final int SCP_02_0A = 5;
    public static final int SCP_02_0B = 6;
    public static final int SCP_02_14 = 7;
    public static final int SCP_02_15 = 8;
    public static final int SCP_02_1A = 9;
    public static final int SCP_02_1B = 10;
}
