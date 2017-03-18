package com.jiangtao.shuzicaimanager.model.person;

import android.content.Intent;
import android.view.View;

import com.jiangtao.shuzicaimanager.AppConfigure;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.basic.manager.ActivityManager;
import com.jiangtao.shuzicaimanager.model.main.MainActivity;

import butterknife.OnClick;

public class PersonActivity extends BaseActivityWithToolBar {


    //设置点击事件
    @OnClick({R.id.manager_user, R.id.logoutBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.logoutBtn:
                startLogout();
                break;

            case R.id.manager_user:
                Intent intent = new Intent(PersonActivity.this, ManagerUserActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    //初始化标头栏
    private void initTitleBar() {
        setLeftImage(R.mipmap.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("个人中心");
    }

    //退出登录
    private void startLogout() {
        Application.userInstance = null;
        AppConfigure.saveLoginStatue(false);
        Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        ActivityManager.getAppManager().finishActivity(MainActivity.class);
    }
}
