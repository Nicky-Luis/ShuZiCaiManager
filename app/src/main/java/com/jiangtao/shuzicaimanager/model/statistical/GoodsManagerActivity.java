package com.jiangtao.shuzicaimanager.model.statistical;

import android.content.Intent;
import android.view.View;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.statistical.goods.HistoryOrderActivity;
import com.jiangtao.shuzicaimanager.model.statistical.goods.NervousGoodsActivity;
import com.jiangtao.shuzicaimanager.model.statistical.goods.OnlineGoodsActivity;
import com.jiangtao.shuzicaimanager.model.statistical.goods.SoldOutActivity;
import com.jiangtao.shuzicaimanager.model.statistical.goods.UnprocessedOrderActivity;

import butterknife.OnClick;

public class GoodsManagerActivity extends BaseActivityWithToolBar {

    @Override
    public int setLayoutId() {
        return R.layout.activity_goods_manager;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    //设置点击事件
    @OnClick({R.id.unprocessed_order_ly, R.id.history_order_ly, R.id.online_goods_ly,
            R.id.nervous_goods_Ly, R.id.sold_out_goods_Ly})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.unprocessed_order_ly: {
                Intent intent = new Intent(GoodsManagerActivity.this, UnprocessedOrderActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.history_order_ly: {
                Intent intent = new Intent(GoodsManagerActivity.this, HistoryOrderActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.online_goods_ly: {
                Intent intent = new Intent(GoodsManagerActivity.this, OnlineGoodsActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.nervous_goods_Ly: {
                Intent intent = new Intent(GoodsManagerActivity.this, NervousGoodsActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.sold_out_goods_Ly: {
                Intent intent = new Intent(GoodsManagerActivity.this, SoldOutActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("商品统计");
    }
}
