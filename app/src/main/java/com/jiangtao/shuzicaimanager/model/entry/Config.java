package com.jiangtao.shuzicaimanager.model.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/5/6.
 * Config
 */
public class Config extends BmobObject {

    //objectId
    public final static String objectId = "JL3B666I";
    //是否允许交易
    private boolean isTread;
    //最新的期数
    private int newestNum;

    public Config() {
    }

    public Config(boolean isTread, int newestNum) {
        this.isTread = isTread;
        this.newestNum = newestNum;
    }

    public boolean isTread() {
        return isTread;
    }

    public int getNewestNum() {
        return newestNum;
    }

    public void setTread(boolean tread) {
        isTread = tread;
    }

    public void setNewestNum(int newestNum) {
        this.newestNum = newestNum;
    }

    @Override
    public String toString() {
        return "Config{" +
                "isTread=" + isTread +
                ", newestNum=" + newestNum +
                '}';
    }
}
