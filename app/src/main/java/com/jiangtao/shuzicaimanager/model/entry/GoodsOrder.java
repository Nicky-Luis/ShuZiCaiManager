package com.jiangtao.shuzicaimanager.model.entry;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Nicky on 2017/2/7.
 * 订单
 */
public class GoodsOrder  extends BmobObject {

    //用户id
    private String userId;
    //订单提交人
    private _User user;
    //商品对象
    private Goods goodObj;
    //财富详情
    private WealthDetail wealthDetail;
    //电话
    private String receivingPhone;
    //收货地址
    private String address;
    //下单时间
    private BmobDate orderTime;
    //联系人
    private String contacts;
    //订单的状态,0：未处理，1:已处理
    private int orderStatus;
    //订单处理人
    private ManagerUser optUser;

    public GoodsOrder(String userId, _User user, Goods goodObj, WealthDetail wealthDetail,
                      String receivingPhone, String address, BmobDate orderTime, String contacts, int orderStatus,
                      ManagerUser optUser) {
        this.userId = userId;
        this.user = user;
        this.goodObj = goodObj;
        this.wealthDetail = wealthDetail;
        this.receivingPhone = receivingPhone;
        this.address = address;
        this.orderTime = orderTime;
        this.contacts = contacts;
        this.orderStatus = orderStatus;
        this.optUser = optUser;
    }

    public GoodsOrder() {
    }

    public String getUserId() {
        return userId;
    }

    public _User getUser() {
        return user;
    }

    public Goods getGoodObj() {
        return goodObj;
    }


    public WealthDetail getWealthDetail() {
        return wealthDetail;
    }

    public String getReceivingPhone() {
        return receivingPhone;
    }

    public String getAddress() {
        return address;
    }

    public BmobDate getOrderTime() {
        return orderTime;
    }

    public String getContacts() {
        return contacts;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public ManagerUser getOptUser() {
        return optUser;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUser(_User user) {
        this.user = user;
    }

    public void setGoodObj(Goods goodObj) {
        this.goodObj = goodObj;
    }


    public void setWealthDetail(WealthDetail wealthDetail) {
        this.wealthDetail = wealthDetail;
    }

    public void setReceivingPhone(String receivingPhone) {
        this.receivingPhone = receivingPhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderTime(BmobDate orderTime) {
        this.orderTime = orderTime;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOptUser(ManagerUser optUser) {
        this.optUser = optUser;
    }

}
