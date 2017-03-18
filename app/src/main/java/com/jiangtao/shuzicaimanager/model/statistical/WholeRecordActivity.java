package com.jiangtao.shuzicaimanager.model.statistical;

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
import com.jiangtao.shuzicaimanager.model.entry.GameInfo;
import com.jiangtao.shuzicaimanager.model.entry.GuessForecastRecord;
import com.jiangtao.shuzicaimanager.model.entry.GuessWholeRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class WholeRecordActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.whole_RecyclerView)
    RecyclerView whole_RecyclerView;

    @BindView(R.id.whole_CountHeader)
    RecyclerViewHeader whole_CountHeader;
    //
    @BindView(R.id.whole_refresh_widget)
    SwipeRefreshLayout whole_refresh_widget;
    //用户数
    @BindView(R.id.whole_CountTxt)
    TextView whole_CountTxt;
    //适配器
    private QuickAdapter<GuessWholeRecord> wholeAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_whole_record;
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
        setCenterTitle("全数预测");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        whole_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        whole_refresh_widget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //两列
        whole_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        whole_CountHeader.attachTo(whole_RecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        whole_RecyclerView.addItemDecoration(decoration);

        //adapter初始化
        wholeAdapter = new QuickAdapter<GuessWholeRecord>(getContext(),
                R.layout.item_whole_record_layout, new ArrayList<GuessWholeRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessWholeRecord item) {
                helper.setText(R.id.whole_time, item.getCreatedAt());
                helper.setText(R.id.whole_name, item.getUserId());
                helper.setText(R.id.whole_forecast, String.valueOf(item.getGuessValue()));
                helper.setText(R.id.whole_money, String.valueOf(item.getGoldValue()));
            }
        };
        whole_RecyclerView.setAdapter(wholeAdapter);
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
                whole_CountTxt.setText("总人数：" + integer);
            }
        });
    }

    /**
     * 获取游戏数据
     */
    private void getGamesData(int periodCount) {
        BmobQuery<GuessWholeRecord> query = new BmobQuery<GuessWholeRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.findObjects(new FindListener<GuessWholeRecord>() {
            @Override
            public void done(List<GuessWholeRecord> list, BmobException e) {
                if (list != null) {
                    wholeAdapter.clear();
                    wholeAdapter.addAll(list);
                }
            }
        });
    }


    //获取最新一期的游戏数据
    private void getNewestGameInfo() {
        BmobQuery<GameInfo> query = new BmobQuery<GameInfo>();
        query.addWhereEqualTo("gameType", GameInfo.type_quanshu);
        query.findObjects(new FindListener<GameInfo>() {
            @Override
            public void done(List<GameInfo> list, BmobException e) {
                if (null != list && list.size() > 0) {
                    int num = list.get(0).getNewestNum();
                    getGamesData(num);
                    getUserCount(num);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        getNewestGameInfo();
    }

}
