package com.jiangtao.shuzicaimanager.model.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseFragment;
import com.jiangtao.shuzicaimanager.model.statistical.UserCountActivity;
import com.jiangtao.shuzicaimanager.model.statistical.WealthDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    //参数
    public static final String ARGS_PAGE = "args_page";

    //设置点击事件
    @OnClick({R.id.rule_set_layout, R.id.virtual_user_layout})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.rule_set_layout: {
                Intent intent = new Intent(getActivity(), UserCountActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.virtual_user_layout: {
                Intent intent = new Intent(getActivity(), WealthDetailActivity.class);
                startActivity(intent);
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
