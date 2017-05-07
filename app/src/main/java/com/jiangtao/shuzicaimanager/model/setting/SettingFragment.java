package com.jiangtao.shuzicaimanager.model.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseFragment;
import com.jiangtao.shuzicaimanager.model.entry.Config;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class SettingFragment extends BaseFragment {

    //参数
    public static final String ARGS_PAGE = "args_page";
    //暂停交易按钮
    @BindView(R.id.stopTreadBtn)
    Button stopTreadBtn;
    //交易状态
    @BindView(R.id.treadTxt)
    TextView treadTxt;

    //设置点击事件
    @OnClick({R.id.rule_set_layout, R.id.virtual_user_layout, R.id.stopTreadBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.rule_set_layout: {
               // Intent intent = new Intent(getActivity(), UserCountActivity.class);
                //startActivity(intent);
            }
            break;

            case R.id.virtual_user_layout: {
                //Intent intent = new Intent(getActivity(), WealthDetailActivity.class);
               // startActivity(intent);
            }
            break;

            //暂停交易设置
            case R.id.stopTreadBtn: {
                Application.appConfig.setTread(!Application.appConfig.isTread());
                Application.appConfig.update(Config.objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (null == e) {
                            ToastUtils.showLongToast("操作成功");
                            if (Application.appConfig.isTread()) {
                                stopTreadBtn.setText("停止交易");
                                treadTxt.setText("正在交易中");
                            } else {
                                stopTreadBtn.setText("开始交易");
                                treadTxt.setText("交易已停止");
                            }
                        } else {
                            ToastUtils.showLongToast("操作失败");
                            LogUtils.e("操作失败" + e);
                        }
                    }
                });
            }
            break;
        }
    }


    //对象实例化
    public static SettingFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        SettingFragment fragment = new SettingFragment();
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
        return R.layout.fragment_setting;
    }

    @Override
    public void loadLayout(View viewDataBinding) {
        getNewestGameInfo();
    }

    //获取最新一期的游戏数据
    private void getNewestGameInfo() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.getObject(Config.objectId, new QueryListener<Config>() {
            @Override
            public void done(Config gameInfo, BmobException e) {
                if (e == null && gameInfo != null) {
                    Application.appConfig = gameInfo;
                    if (Application.appConfig.isTread()) {
                        stopTreadBtn.setText("停止交易");
                        treadTxt.setText("正在交易中");
                    } else {
                        stopTreadBtn.setText("开始交易");
                        treadTxt.setText("交易已停止");
                    }
                } else {
                    LogUtils.e("获取配置信息失败:" + e);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewestGameInfo();
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
}
