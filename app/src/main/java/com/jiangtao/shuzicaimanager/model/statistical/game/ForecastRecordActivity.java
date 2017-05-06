package com.jiangtao.shuzicaimanager.model.statistical.game;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.common.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.Config;
import com.jiangtao.shuzicaimanager.model.entry.GuessForecastRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class ForecastRecordActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.up_down_RecyclerView)
    RecyclerView up_down_RecyclerView;

    @BindView(R.id.up_down_CountHeader)
    RecyclerViewHeader up_down_CountHeader;
    //
    @BindView(R.id.up_down_refresh_widget)
    SwipeRefreshLayout up_down_refresh_widget;
    //用户数
    @BindView(R.id.up_down_CountTxt)
    TextView up_down_CountTxt;
    //适配器
    private QuickAdapter<GuessForecastRecord> forecastAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_forecast_record;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        getNewestGameInfo();
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
        setCenterTitle("涨跌预测详情");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        up_down_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        up_down_refresh_widget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //两列
        up_down_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        up_down_CountHeader.attachTo(up_down_RecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        up_down_RecyclerView.addItemDecoration(decoration);

        //adapter初始化
        forecastAdapter = new QuickAdapter<GuessForecastRecord>(getContext(),
                R.layout.item_up_down_count_layout, new ArrayList<GuessForecastRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessForecastRecord item) {
                helper.setText(R.id.up_down_time, item.getCreatedAt());
                helper.setText(R.id.up_down_time_name, "用户：" + item.getUserId());
                helper.setText(R.id.up_down_forecast, item.getBetValue() == 1 ? "看涨" : "看跌");
                helper.setText(R.id.up_down_money, item.getBetSilverValue() + "银币");
            }
        };
        up_down_RecyclerView.setAdapter(forecastAdapter);
    }


    /**
     * 获取总数
     */
    private void getUserCount(int periodCount) {
        BmobQuery<GuessForecastRecord> query = new BmobQuery<GuessForecastRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                up_down_CountTxt.setText("总人数：" + count);
            }
        });
    }

    /**
     * 获取游戏数据
     */
    private void getGamesData(int periodCount) {
        BmobQuery<GuessForecastRecord> query = new BmobQuery<GuessForecastRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.findObjects(new FindListener<GuessForecastRecord>() {
            @Override
            public void done(List<GuessForecastRecord> list, BmobException e) {
                if (list != null) {
                    forecastAdapter.clear();
                    forecastAdapter.addAll(list);
                }
            }
        });
    }


    //获取最新一期的游戏数据
    private void getNewestGameInfo() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.getObject(Config.objectId, new QueryListener<Config>() {
            @Override
            public void done(Config gameInfo, BmobException e) {
                if (e == null && gameInfo != null) {
                    Application.appConfig = gameInfo;
                    int num = gameInfo.getNewestNum();
                    setCenterTitle("涨跌预测第(" + num + ")期");
                    getGamesData(num);
                    getUserCount(num);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getNewestGameInfo();
    }


}
