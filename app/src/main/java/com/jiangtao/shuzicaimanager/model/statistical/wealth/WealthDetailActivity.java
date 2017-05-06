package com.jiangtao.shuzicaimanager.model.statistical.wealth;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.common.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.WealthDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WealthDetailActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.wealthRecyclerView)
    RecyclerView wealthRecyclerView;

    @BindView(R.id.wealthCountHeader)
    RecyclerViewHeader wealthHeader;
    //
    @BindView(R.id.wealth_count_refresh_widget)
    SwipeRefreshLayout wealthRefreshWidget;
    //财富总数
    @BindView(R.id.wealthCountTxt)
    TextView wealthCountTxt;
    //适配器
    private QuickAdapter<WealthDetail> wealthAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_wealth_detail;
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
        setCenterTitle("财务统计");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        wealthRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        wealthRefreshWidget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //1列
        wealthRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        wealthHeader.attachTo(wealthRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        wealthRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        wealthAdapter = new QuickAdapter<WealthDetail>(getContext(),
                R.layout.item_wealth_count_layout, new ArrayList<WealthDetail>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final WealthDetail item) {
                helper.setText(R.id.wealth_date_txt, item.getCreatedAt());
                helper.setText(R.id.wealthUserName, "充值金额");
                helper.setText(R.id.wealthCount, item.getOperationValue() + "元");
                helper.setText(R.id.transactionId, item.getObjectId());
            }
        };
        wealthRecyclerView.setAdapter(wealthAdapter);
    }

    /***
     * 获取充值数据
     */
    private void getWealthDetail() {
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("operationType", WealthDetail.Operation_Type_Recharge);
        query.setLimit(1000);
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                wealthRefreshWidget.setRefreshing(false);
                int count = 0;
                if (null != list && list.size() > 0) {
                    for (WealthDetail wealth : list) {
                        count = count + wealth.getOperationValue();
                    }
                    wealthAdapter.clear();
                    wealthAdapter.addAll(list);
                }
                wealthCountTxt.setText("总金额：" + count);
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        getWealthDetail();
    }

}
