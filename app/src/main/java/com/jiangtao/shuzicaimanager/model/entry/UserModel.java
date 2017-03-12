package com.jiangtao.shuzicaimanager.model.entry;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Nicky on 2017/1/19.
 * 用户
 */

public class UserModel extends BmobUser {

    private static final long serialVersionUID = 1L;
    //昵称
    private String nickName;
    //头像
    private String headImageUrl;
    //地址
    private String address;
    //性别
    private int gender;
    //注册时间
    private BmobDate createdAt;
    //手机号
    private String mobilePhoneNumber;

    public String getNickName() {
        return nickName;
    }

    public UserModel(String nickName, String headImageUrl) {
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
    }

    public UserModel(String nickName, String headImageUrl, String address, int gender, BmobDate createdAt, String
            mobilePhoneNumber) {
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
        this.address = address;
        this.gender = gender;
        this.createdAt = createdAt;
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "nickName='" + nickName + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                '}';
    }
}
