package com.jiangtao.shuzicaimanager.model.person;

import android.content.Intent;
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
import com.jiangtao.shuzicaimanager.model.entry.ManagerUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ManagerUserActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.manager_user_orderRecyclerView)
    RecyclerView manager_user_orderRecyclerView;
    //
    @BindView(R.id.manager_user_refresh_widget)
    SwipeRefreshLayout manager_user_refresh_widget;
    //适配器
    private QuickAdapter<ManagerUser> goodsAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_manager_user;
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
                Intent intent = new Intent(ManagerUserActivity.this, AddManagerUserActivity.class);
                startActivity(intent);
            }
        });
        setCenterTitle("管理员列表");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        manager_user_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        manager_user_refresh_widget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        manager_user_orderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        manager_user_orderRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        goodsAdapter = new QuickAdapter<ManagerUser>(getContext(),
                R.layout.item_manager_user_layout, new ArrayList<ManagerUser>()) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final ManagerUser item) {
                helper.setText(R.id.item_manager_user_name, "名字：" + item.getName());
                helper.setText(R.id.item_manager_user_account, "账户：" + item.getAccount());

                //长按
                helper.setOnLongClickListener(R.id.online_goods_item_root, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
            }
        };
        manager_user_orderRecyclerView.setAdapter(goodsAdapter);
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
        BmobQuery<ManagerUser> query = new BmobQuery<ManagerUser>();
        query.setLimit(500);
        query.findObjects(new FindListener<ManagerUser>() {
            @Override
            public void done(List<ManagerUser> list, BmobException e) {
                manager_user_refresh_widget.setRefreshing(false);
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
