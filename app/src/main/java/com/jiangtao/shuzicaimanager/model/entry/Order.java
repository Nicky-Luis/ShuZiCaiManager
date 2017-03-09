package com.jiangtao.shuzicaimanager.model.entry;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * Created by Nicky on 2017/2/7.
 * 订单
 */
public class Order extends BmobObject {

    //商品Pointer
    private BmobPointer goods;
    //用户Id
    private String userId;
    //电话
    private String receivingPhone;
    //收货地址
    private String address;
    //下单时间
    private BmobDate orderTime;
    //联系人
    private String contacts;
    //商品对象
    private Goods goodObj;

    public void setOrderTime(BmobDate orderTime) {
        this.orderTime = orderTime;
    }

    public BmobDate getOrderTime() {
        return orderTime;
    }

    //订单的状态,0：未处理，1:已处理
    private int orderStatus;

    public Order() {
        this.setTableName("goodOrder");
    }

    public BmobPointer getGoods() {
        return goods;
    }


    public String getReceivingPhone() {
        return receivingPhone;
    }

    public String getAddress() {
        return address;
    }


    public int getOrderStatus() {
        return orderStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setGoods(BmobPointer goods) {

        this.goods = goods;
    }

    public void setReceivingPhone(String receivingPhone) {
        this.receivingPhone = receivingPhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Goods getGoodObj() {
        return goodObj;
    }

    public void setGoodObj(Goods goodObj) {
        this.goodObj = goodObj;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
