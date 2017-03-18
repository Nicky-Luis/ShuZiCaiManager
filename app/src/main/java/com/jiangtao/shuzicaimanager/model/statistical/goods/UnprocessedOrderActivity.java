package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.common.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.GoodsOrder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class UnprocessedOrderActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.unprocessed_orderRecyclerView)
    RecyclerView unprocessed_orderRecyclerView;
    //
    @BindView(R.id.unprocessed_order_refresh_widget)
    SwipeRefreshLayout unprocessed_order_refresh_widget;
    //适配器
    private QuickAdapter<GoodsOrder> ordersAapter;


    @Override
    public int setLayoutId() {
        return R.layout.activity_unprocessed_order;
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
        setCenterTitle("待处理订单");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        unprocessed_order_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        unprocessed_order_refresh_widget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //两列
        unprocessed_orderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        unprocessed_orderRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        ordersAapter = new QuickAdapter<GoodsOrder>(getContext(),
                R.layout.item_unprocessed_order_layout, new ArrayList<GoodsOrder>()) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final GoodsOrder item) {
                helper.setText(R.id.item_unprocessed_time, item.getCreatedAt());
                helper.setText(R.id.goods_type, "虚拟商品");
                helper.setImageUrl(R.id.goodsImg, item.getGoodObj().getGoodsImgUrl());
                helper.setText(R.id.item_goods_name, item.getGoodObj().getGoodsName());
                helper.setText(R.id.goods_price, item.getGoodObj().getGoodsPrice() + "金币");
                helper.setText(R.id.user_name, "兑换人：" + item.getContacts());
                helper.setText(R.id.user_account, "账户：" + item.getReceivingPhone());
                helper.setText(R.id.order_status, "状态：" + (item.getOrderStatus() == 0 ? "未处理" : "已处理"));
                helper.setOnClickListener(R.id.set_order_flag_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item.setOptUser(Application.userInstance);
                        item.setOrderStatus(1);
                        showProgress();
                        item.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                hideProgress();
                                if (e == null) {
                                    helper.setBtnText(R.id.order_status, "标记为未处理");
                                }
                            }
                        });
                    }
                });
            }
        };
        unprocessed_orderRecyclerView.setAdapter(ordersAapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }


    private void initData() {
        getOrdersValue();
    }

    /***
     * 获取所有未处理订单
     */
    private void getOrdersValue() {
        BmobQuery<GoodsOrder> query = new BmobQuery<GoodsOrder>();
        query.addWhereEqualTo("orderStatus", 0);
        query.setLimit(100);
        query.include("user");
        query.include("goodObj");
        query.findObjects(new FindListener<GoodsOrder>() {
            @Override
            public void done(List<GoodsOrder> list, BmobException e) {
                unprocessed_order_refresh_widget.setRefreshing(false);
                if (null != list) {
                    LogUtils.i("数据数为：" + list.size());
                    ordersAapter.clear();
                    ordersAapter.addAll(list);

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
