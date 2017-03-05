package com.jiangtao.shuzicaimanager.model.main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.basic.manager.ActivityManager;
import com.jiangtao.shuzicaimanager.model.person.PersonActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivityWithToolBar {

    //记录用户首次点击返回键的时间
    private long firstBackTime = 0;

    //viewpager
    @BindView(R.id.mainViewPager)
    ViewPager mainViewPager;
    //tabs
    @BindView(R.id.mainTabs)
    TabLayout mainTabs;

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitialize() {
        resetToolView();
        setLeftTitle("数字连连猜");
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(
                getBaseFragmentManager(), getContext());
        mainViewPager.setAdapter(adapter);
        setRightTitle("我的", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转至设置页面
                startActivity(new Intent(MainActivity.this, PersonActivity.class));
            }
        });
        mainTabs.setupWithViewPager(mainViewPager);
        initTabView(adapter);
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 初始化底部tabview
     *
     * @param adapter
     */
    private void initTabView(MainFragmentPagerAdapter adapter) {
        for (int i = 0; i < mainTabs.getTabCount(); i++) {
            TabLayout.Tab tab = mainTabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstBackTime > 2000) {
                    showToast(R.string.MainActivity_key_back_text);
                    firstBackTime = secondTime;
                    return true;
                } else {
                    ActivityManager.getAppManager().AppExit(this, true);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
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
