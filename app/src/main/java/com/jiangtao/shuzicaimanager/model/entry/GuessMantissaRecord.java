package com.jiangtao.shuzicaimanager.model.entry;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Nicky on 2017/2/19.
 * 尾数预测记录
 */

public class GuessMantissaRecord extends BmobObject {
    //游戏类型
    public final static int Guess_Type_Percentile = 0;
    public final static int Guess_Type_DoubleDirect = 1;
    public final static int Guess_Type_DoubleGroup = 2;
    //指数类型
    public final static int Index_Type_Hushen = 0;
    public final static int Index_Type_Gold = 1;
    //用户id
    private String userId;
    //押注金币数值
    private int goldValue;
    //押注时间
    private BmobDate time;
    //类型
    private int guessType;
    //指数类型
    private int indexType;
    //押注期数
    private int periodNum;
    //押注的数据
    private int guessValue;
    //实际的指数
    private float indexResult;
    //是否中奖
    private boolean isReward;
    //获取的奖励数量
    private float rewardCount;

    public GuessMantissaRecord(String userId, int goldValue, BmobDate time, int guessType, int indexType, int
            periodNum, int guessValue, float indexResult, boolean isReward, float rewardCount) {
        this.userId = userId;
        this.goldValue = goldValue;
        this.time = time;
        this.guessType = guessType;
        this.indexType = indexType;
        this.periodNum = periodNum;
        this.guessValue = guessValue;
        this.indexResult = indexResult;
        this.isReward = isReward;
        this.rewardCount = rewardCount;
    }

    public GuessMantissaRecord() {
    }

    public String getUserId() {
        return userId;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public BmobDate getTime() {
        return time;
    }

    public int getGuessType() {
        return guessType;
    }

    public int getIndexType() {
        return indexType;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public int getGuessValue() {
        return guessValue;
    }

    public float getIndexResult() {
        return indexResult;
    }

    public boolean isReward() {
        return isReward;
    }

    public float getRewardCount() {
        return rewardCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public void setTime(BmobDate time) {
        this.time = time;
    }

    public void setGuessType(int guessType) {
        this.guessType = guessType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public void setGuessValue(int guessValue) {
        this.guessValue = guessValue;
    }

    public void setIndexResult(float indexResult) {
        this.indexResult = indexResult;
    }

    public void setReward(boolean reward) {
        isReward = reward;
    }

    public void setRewardCount(float rewardCount) {
        this.rewardCount = rewardCount;
    }
}
