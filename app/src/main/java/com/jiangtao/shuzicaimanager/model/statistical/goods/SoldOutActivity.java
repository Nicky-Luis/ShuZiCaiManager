package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.common.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.Goods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SoldOutActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sold_out_orderRecyclerView)
    RecyclerView sold_out_orderRecyclerView;
    //
    @BindView(R.id.sold_out_order_refresh_widget)
    SwipeRefreshLayout sold_out_order_refresh_widget;
    //适配器
    private QuickAdapter<Goods> goodsAdapter;
    @Override
    public int setLayoutId() {
        return R.layout.activity_sold_out;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        initData();
    }

    @Override
    public void initPresenter() {

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
        setRightTitle("添加", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoldOutActivity.this, AddGoodsActivity.class);
                startActivity(intent);
            }
        });
        setCenterTitle("库存为空的商品");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        sold_out_order_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        sold_out_order_refresh_widget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //两列
        sold_out_orderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        sold_out_orderRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        goodsAdapter = new QuickAdapter<Goods>(getContext(),
                R.layout.item_sold_out_goods_layout, new ArrayList<Goods>()) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final Goods item) {
                helper.setImageUrl(R.id.sold_out_goods_img, item.getGoodsImgUrl());
                helper.setText(R.id.sold_out_goods_name, item.getGoodsName());
                helper.setText(R.id.sold_out_goods_remain, "库存为空");
                helper.setText(R.id.sold_out_goods_price, "价格：" + item.getGoodsPrice() + "金币");

                helper.setOnClickListener(R.id.sold_out_goods_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SoldOutActivity.this, GoodsInfoActivity.class);
                        Bundle bd = new Bundle();
                        bd.putSerializable(GoodsInfoActivity.Intent_Key, item);
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });
            }
        };
        sold_out_orderRecyclerView.setAdapter(goodsAdapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }


    private void initData() {
        showProgress();
        getOrdersValue();
    }

    /***
     * 获取所有未处理订单
     */
    private void getOrdersValue() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("inventory", 0);
        query.setLimit(500);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                hideProgress();
                sold_out_order_refresh_widget.setRefreshing(false);
                if (null != list) {
                    LogUtils.i("数据数为：" + list.size());
                    goodsAdapter.clear();
                    goodsAdapter.addAll(list);

                    if (list.size() == 0) {
                        ToastUtils.showShortToast("数据为空");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
