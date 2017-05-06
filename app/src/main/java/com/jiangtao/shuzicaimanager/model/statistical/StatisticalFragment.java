package com.jiangtao.shuzicaimanager.model.statistical;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseFragment;
import com.jiangtao.shuzicaimanager.model.entry.Config;
import com.jiangtao.shuzicaimanager.model.entry.Goods;
import com.jiangtao.shuzicaimanager.model.entry.GuessForecastRecord;
import com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord;
import com.jiangtao.shuzicaimanager.model.entry.GuessWholeRecord;
import com.jiangtao.shuzicaimanager.model.entry.WealthDetail;
import com.jiangtao.shuzicaimanager.model.statistical.game.ForecastRecordActivity;
import com.jiangtao.shuzicaimanager.model.statistical.game.MantissaRecordActivity;
import com.jiangtao.shuzicaimanager.model.statistical.game.WholeRecordActivity;
import com.jiangtao.shuzicaimanager.model.statistical.goods.GoodsManagerActivity;
import com.jiangtao.shuzicaimanager.model.statistical.user.UserCountActivity;
import com.jiangtao.shuzicaimanager.model.statistical.wealth.WealthDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Nicky on 2017/3/5.
 * t
 */
public class StatisticalFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {
    //参数
    public static final String ARGS_PAGE = "args_page";
    @BindView(R.id.main_root_view)
    SwipeRefreshLayout refreshLayout;
    //用户数据
    @BindView(R.id.main_user_count)
    TextView main_user_count;
    //今日新增
    @BindView(R.id.main_user_added)
    TextView main_user_added;
    //财富
    @BindView(R.id.wealth_value_txt)
    TextView wealth_value_txt;
    //充值人数
    @BindView(R.id.recharge_count_txt)
    TextView recharge_count_txt;
    //总充值金额
    @BindView(R.id.recharge_value_txt)
    TextView recharge_value_txt;
    //参与人数
    @BindView(R.id.join_person_count_txt)
    TextView join_person_count_txt;
    //猜涨人数
    @BindView(R.id.join_up_count_txt)
    TextView join_up_count_txt;
    //猜跌人数
    @BindView(R.id.join_down_count_txt)
    TextView join_down_count_txt;
    //猜尾数
    @BindView(R.id.weishu_info_txt)
    TextView weishu_info_txt;
    //猜全数
    @BindView(R.id.quanshu_info_txt)
    TextView quanshu_info_txt;
    //商品总数
    @BindView(R.id.goods_count_txt)
    TextView goods_count_txt;
    //库存紧张
    @BindView(R.id.goods_not_enough_txt)
    TextView goods_not_enough_txt;
    //库存紧张
    @BindView(R.id.goods_zero_txt)
    TextView goods_zero_txt;

    //设置点击事件
    @OnClick({R.id.userStatisticsLy, R.id.wealthStatisticsLy, R.id.upDownStatisticsLy,
            R.id.mantissaStatisticsLy, R.id.wholeStatisticsLy, R.id.goodsStatisticsLy,
            R.id.goodsStatisticsLy1, R.id.userStatisticsLy1, R.id.wealthStatisticsLy1,
            R.id.upDownStatisticsLy1})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.userStatisticsLy1:
            case R.id.userStatisticsLy: {
                Intent intent = new Intent(getActivity(), UserCountActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.wealthStatisticsLy1:
            case R.id.wealthStatisticsLy: {
                Intent intent = new Intent(getActivity(), WealthDetailActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.upDownStatisticsLy1:
            case R.id.upDownStatisticsLy: {
                Intent intent = new Intent(getActivity(), ForecastRecordActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.mantissaStatisticsLy: {
                Intent intent = new Intent(getActivity(), MantissaRecordActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.wholeStatisticsLy: {
                Intent intent = new Intent(getActivity(), WholeRecordActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.goodsStatisticsLy1:
            case R.id.goodsStatisticsLy: {
                Intent intent = new Intent(getActivity(), GoodsManagerActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    //对象实例化
    public static StatisticalFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        StatisticalFragment fragment = new StatisticalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGetArgument() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_statistical;
    }

    @Override
    public void loadLayout(View viewDataBinding) {
        initSwipeRefresh();
        getData();
    }

    private void getData() {
        //用户总数
        getUserCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //财务管理
                getWealthValue();
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //游戏数据
                getNewestGameInfo();
            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取商品
                getGoodsInfo();
            }
        }, 600);
    }

    //初始化swipe
    private void initSwipeRefresh() {
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }

    /////////////////////////////////////////////////////////

    /**
     * 获取用户总数
     */
    private void getUserCount() {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.count(BmobUser.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    main_user_count.setText("总用户：" + count);
                    //今日新增人数
                    getTodayAdded();
                } else {
                    main_user_count.setText("总用户：0");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /***
     * 获取今日新增用户数
     */
    private void getTodayAdded() {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        Calendar now = Calendar.getInstance();
        query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(new Date(now.get(Calendar.YEAR), now.get
                (Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))));
        query.count(BmobUser.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    main_user_added.setText("今日新增：" + count);
                } else {
                    main_user_count.setText("今日新增：0");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /***
     * 获取所有财富数据
     */
    private void getWealthValue() {
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("operationType", WealthDetail.Operation_Type_Recharge);
        query.setLimit(1000);
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    int count = 0;
                    if (null != list) {
                        for (WealthDetail wealth : list) {
                            count = count + wealth.getOperationValue();
                        }
                    }
                    wealth_value_txt.setText("总金额：" + count);
                    //今日充值人数
                    getRechargePeopleCount();
                    //今日充值金额
                    getTodayWealthValue();
                }
            }
        });
    }

    /***
     * 获取今日充值数据
     */
    private void getRechargePeopleCount() {
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("operationType", WealthDetail.Operation_Type_Recharge);
        Calendar now = Calendar.getInstance();
        query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(new Date(now.get(Calendar.YEAR), now.get
                (Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))));
        query.count(WealthDetail.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    recharge_count_txt.setText("充值：" + count + "人");
                } else {
                    recharge_count_txt.setText("充值：0人");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /***
     * 获取今日充值数据
     */
    private void getTodayWealthValue() {
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("operationType", WealthDetail.Operation_Type_Recharge);
        Calendar now = Calendar.getInstance();
        query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(new Date(now.get(Calendar.YEAR), now.get
                (Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))));
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                refreshLayout.setRefreshing(false);
                int count = 0;
                if (null != list) {
                    for (WealthDetail wealth : list) {
                        count = count + wealth.getOperationValue();
                    }
                }
                recharge_value_txt.setText("今日：" + count + "元");
            }
        });
    }

    //获取最新一期的游戏数据
    private void getNewestGameInfo() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.getObject(Config.objectId, new QueryListener<Config>() {
            @Override
            public void done(Config gameInfo, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null && gameInfo != null) {
                    Application.appConfig = gameInfo;
                    int num = gameInfo.getNewestNum();
                    getGuessForecastRecordCount(num);
                    getGuessMantissaRecordCount(num);
                    getGuessWholeRecordCount(num);
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                }
            }
        });
    }

    //猜涨跌
    private void getGuessForecastRecordCount(int periodCount) {
        BmobQuery<GuessForecastRecord> query = new BmobQuery<GuessForecastRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (null == integer) {
                    join_person_count_txt.setText("总人数：0");
                } else {
                    join_person_count_txt.setText("总人数：" + integer);
                }
            }
        });

        //查询猜涨人数
        BmobQuery<GuessForecastRecord> query1 = new BmobQuery<GuessForecastRecord>();
        query1.addWhereEqualTo("periodNum", periodCount);
        query1.addWhereEqualTo("betValue", 1);
        query1.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (null == integer) {
                    join_up_count_txt.setText("猜涨：0");
                } else {
                    join_up_count_txt.setText("猜涨：" + integer);
                }
            }
        });

        //查询猜跌人数
        BmobQuery<GuessForecastRecord> query2 = new BmobQuery<GuessForecastRecord>();
        query2.addWhereEqualTo("periodNum", periodCount);
        query2.addWhereEqualTo("betValue", 0);
        query2.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (null == integer) {
                    join_down_count_txt.setText("猜跌：0");
                } else {
                    join_down_count_txt.setText("猜跌：" + integer);
                }
            }
        });
    }

    //猜尾数
    private void getGuessMantissaRecordCount(final int periodCount) {
        BmobQuery<GuessMantissaRecord> query = new BmobQuery<GuessMantissaRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.count(GuessMantissaRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                weishu_info_txt.setText("尾数（" + periodCount + "期）   参与总数：" + count);
            }
        });
    }

    //猜全数
    private void getGuessWholeRecordCount(final int periodCount) {
        BmobQuery<GuessWholeRecord> query = new BmobQuery<GuessWholeRecord>();
        query.addWhereEqualTo("periodNum", periodCount);
        query.count(GuessWholeRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                quanshu_info_txt.setText("全数（" + periodCount + "期）   参与总数：" + count);
            }
        });
    }


    //获取商品信息
    private void getGoodsInfo() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.count(Goods.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    int count = 0;
                    if (null != integer) {
                        count = integer;
                    }
                    goods_count_txt.setText("商品总数：" + count);
                    startGetNervousGoods();
                    startGetNullGoods();
                }
            }
        });
    }

    //查询库存为空的商品
    private void startGetNullGoods() {
        BmobQuery<Goods> query2 = new BmobQuery<Goods>();
        query2.addWhereLessThanOrEqualTo("inventory", 0);
        query2.count(Goods.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                goods_zero_txt.setText("库存为空：" + count);
            }
        });
    }

    //查询紧张的库存
    private void startGetNervousGoods() {
        BmobQuery<Goods> query1 = new BmobQuery<Goods>();
        query1.addWhereLessThanOrEqualTo("inventory", 5);
        query1.addWhereGreaterThan("inventory", 0);
        query1.count(Goods.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                refreshLayout.setRefreshing(false);
                int count = 0;
                if (null != integer) {
                    count = integer;
                }
                goods_not_enough_txt.setText("库存紧张：" + count);
            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
