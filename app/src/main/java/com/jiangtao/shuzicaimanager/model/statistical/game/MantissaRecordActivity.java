package com.jiangtao.shuzicaimanager.model.statistical.game;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.common.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.Config;
import com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord.Guess_Type_DoubleDirect;
import static com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord.Guess_Type_DoubleGroup;
import static com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord.Guess_Type_Percentile;

public class MantissaRecordActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {
    //公告栏
    @BindView(R.id.mantissaRadioGroup)
    RadioGroup mantissaRadioGroup;
    @BindView(R.id.mantissaRecyclerView)
    RecyclerView mantissaRecyclerView;
    //
    @BindView(R.id.mantissaHeader)
    RecyclerViewHeader mantissaHeader;
    //
    @BindView(R.id.mantissa_refresh_widget)
    SwipeRefreshLayout mantissa_refresh_widget;
    //用户数
    @BindView(R.id.join_person_CountTxt)
    TextView join_person_CountTxt;
    //适配器
    private QuickAdapter<GuessMantissaRecord> mantissAdapter;
    //当前的游戏类型
    private int currentGuessType = GuessMantissaRecord.Guess_Type_Percentile;


    @Override
    public int setLayoutId() {
        return R.layout.activity_mantissa_record;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        setViewValue();
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
        setCenterTitle("尾数预测");
    }


    //初始化swipe
    private void initSwipeRefresh() {
        mantissa_refresh_widget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mantissa_refresh_widget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        mantissaRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        mantissaHeader.attachTo(mantissaRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        mantissaRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        mantissAdapter = new QuickAdapter<GuessMantissaRecord>(getContext(),
                R.layout.item_mantissa_count_layout, new ArrayList<GuessMantissaRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessMantissaRecord item) {
                helper.setText(R.id.mantiss_time, item.getCreatedAt());
                helper.setText(R.id.mantiss_name, "用户：" + item.getUserId());
                helper.setText(R.id.mantiss_forecast, "预测值：" + String.valueOf(item.getGuessValue()));
                helper.setText(R.id.mantiss_money, item.getGoldValue() + "金币");
            }
        };
        mantissaRecyclerView.setAdapter(mantissAdapter);
    }


    /**
     * 获取总数
     */
    private void getUserCount(int periodCount) {
        BmobQuery<GuessMantissaRecord> query = new BmobQuery<GuessMantissaRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.addWhereEqualTo("guessType", currentGuessType);
        query.count(GuessMantissaRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                mantissa_refresh_widget.setRefreshing(false);
                join_person_CountTxt.setText("总人数：" + integer);
            }
        });
    }

    /**
     * 获取游戏数据
     */
    private void getGamesData(int periodCount) {
        BmobQuery<GuessMantissaRecord> query = new BmobQuery<GuessMantissaRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.addWhereEqualTo("guessType", currentGuessType);
        query.findObjects(new FindListener<GuessMantissaRecord>() {
            @Override
            public void done(List<GuessMantissaRecord> list, BmobException e) {
                mantissa_refresh_widget.setRefreshing(false);
                if (list != null) {
                    mantissAdapter.clear();
                    mantissAdapter.addAll(list);
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
                    setCenterTitle("尾数预测第(" + num + ")期");
                    getGamesData(num);
                    getUserCount(num);
                }
            }
        });
    }

    //设置view的值
    private void setViewValue() {
        //点击事件
        mantissaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.mantissaFirstBtn:
                        currentGuessType = Guess_Type_Percentile;
                        break;

                    case R.id.mantissaSecondBtn:
                        currentGuessType = Guess_Type_DoubleDirect;
                        break;

                    case R.id.mantissaThirdBtn:
                        currentGuessType = Guess_Type_DoubleGroup;
                        break;
                }
                getNewestGameInfo();
            }
        });
    }

    @Override
    public void onRefresh() {
        getNewestGameInfo();
    }
}
