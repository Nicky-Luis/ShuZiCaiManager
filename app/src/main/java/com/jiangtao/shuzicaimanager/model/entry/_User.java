package com.jiangtao.shuzicaimanager.model.entry;

import cn.bmob.v3.BmobUser;

/**
 * Created by Nicky on 2017/1/19.
 * 用户
 */

public class _User extends BmobUser {

    //昵称
    private String nickName;
    //token
    private String token;
    //受邀请码
    private String InviteeCode;
    //邀请码
    private String InvitationCode;
    //头像
    private String headImageUrl;
    //性别
    private int gender;
    //地址
    private String address;

    public _User(String nickName, String token, String inviteeCode, String invitationCode, String headImageUrl,
                 int gender, String address) {
        this.nickName = nickName;
        this.token = token;
        InviteeCode = inviteeCode;
        InvitationCode = invitationCode;
        this.headImageUrl = headImageUrl;
        this.gender = gender;
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public String getToken() {
        return token;
    }

    public String getInviteeCode() {
        return InviteeCode;
    }

    public String getInvitationCode() {
        return InvitationCode;
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

    public void setToken(String token) {
        this.token = token;
    }

    public void setInviteeCode(String inviteeCode) {
        InviteeCode = inviteeCode;
    }

    public void setInvitationCode(String invitationCode) {
        InvitationCode = invitationCode;
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
