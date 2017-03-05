package com.jiangtao.shuzicaimanager.model.statistical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by Nicky on 2017/3/5.
 */

public class StatisticalFragment extends BaseFragment {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //用户数据
    @BindView(R.id.main_user_count)
    TextView main_user_count;
    //今日新增
    @BindView(R.id.main_user_added)
    TextView main_user_added;
    //今日活跃
    @BindView(R.id.main_user_activity)
    TextView main_user_activity;

    //设置点击事件
    @OnClick({R.id.userStatisticsLy})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.userStatisticsLy: {
                Intent intent = new Intent(getActivity(), UserCountActivity.class);
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
        getUserCount();
        getTodayAdded();
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
                if (e == null) {
                    main_user_count.setText("总用户：" + count);
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
                if (e == null) {
                    main_user_added.setText("今日新增：" + count);
                } else {
                    main_user_count.setText("今日新增：0");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
