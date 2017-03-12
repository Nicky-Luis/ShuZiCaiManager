package com.jiangtao.shuzicaimanager.model.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/6.
 * l
 */
public class WealthValue extends BmobObject {

    //用户id
    private String userId;
    //银币数量
    private float silverValue;
    //金币数量
    private float goldValue;

    public WealthValue(String userId, float silverValue, float goldValue) {
        this.userId = userId;
        this.silverValue = silverValue;
        this.goldValue = goldValue;
    }

    public WealthValue() {
        this.setTableName("WealthValue");
    }

    public String getUserId() {
        return userId;
    }

    public float getSilverValue() {
        return silverValue;
    }

    public float getGoldValue() {
        return goldValue;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSilverValue(float silverValue) {
        this.silverValue = silverValue;
    }

    public void setGoldValue(float goldValue) {
        this.goldValue = goldValue;
    }
}
