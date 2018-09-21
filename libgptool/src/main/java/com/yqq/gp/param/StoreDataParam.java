package com.yqq.gp.param;

import java.util.List;

import pro.javacard.gp.AID;

public class StoreDataParam {
    private AID aid = null;
    private List<byte[]> blocks = null;
    private byte[] originDat = null;
    private int curBlockIndex = 0;

    public StoreDataParam(AID aid, byte[] data) {
        this.aid = aid;
        originDat = data;
    }

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public List<byte[]> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<byte[]> blocks) {
        this.blocks = blocks;
    }

    public byte[] getOriginDat() {
        return originDat;
    }

    public void setOriginDat(byte[] originDat) {
        this.originDat = originDat;
    }

    public int getCurBlockIndex() {
        return curBlockIndex;
    }

    public void inCurBlockIndex() {
        this.curBlockIndex++;
    }

    public void setCurBlockIndex(int curBlockIndex) {
        this.curBlockIndex = curBlockIndex;
    }
}
