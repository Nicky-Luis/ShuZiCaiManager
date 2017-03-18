package com.jiangtao.shuzicaimanager.model.entry;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/18.
 * 虚拟用户
 */

public class VirtualUser extends BmobObject implements Serializable{
    //昵称
    private String nickName;
    //头像
    private String headImageUrl;
    //性别
    private int gender;
    //地址
    private String address;

    public VirtualUser(String nickName, String headImageUrl, int gender, String address) {
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
        this.gender = gender;
        this.address = address;
    }

    public VirtualUser() {
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
