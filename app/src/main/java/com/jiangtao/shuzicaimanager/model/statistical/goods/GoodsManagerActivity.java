package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.GoodsOrder;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

public class GoodsManagerActivity extends BaseActivityWithToolBar {

    @BindView(R.id.unprocessed_count)
    TextView unprocessed_count;

    @Override
    public int setLayoutId() {
        return R.layout.activity_goods_manager;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        getOrdersCount();
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

    @Override
    protected void onResume() {
        super.onResume();
        getOrdersCount();
    }

    /**
     * 获取待处理订单总数
     */
    private void getOrdersCount() {
        BmobQuery<GoodsOrder> query = new BmobQuery<GoodsOrder>();
        query.addWhereEqualTo("orderStatus", 0);
        query.count(GoodsOrder.class, new CountListener() {
            @Override
            public void done(final Integer integer, BmobException e) {
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                if (count > 0) {
                    unprocessed_count.setText(String.valueOf(count));
                }
            }
        });
    }
}
