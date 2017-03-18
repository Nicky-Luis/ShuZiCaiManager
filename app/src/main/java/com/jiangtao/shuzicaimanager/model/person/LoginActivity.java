package com.jiangtao.shuzicaimanager.model.person;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.AppConfigure;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.basic.utils.EditTextUtils;
import com.jiangtao.shuzicaimanager.model.entry.ManagerUser;
import com.jiangtao.shuzicaimanager.model.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends BaseActivityWithToolBar {

    //账户
    @BindView(R.id.loginPhoneEdt)
    EditText loginPhoneEdt;
    //密码
    @BindView(R.id.loginPasswordEdt)
    EditText loginPasswordEdt;
    //用户名
    private String account;
    //密码
    private String password;

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        if (AppConfigure.userIsLogin()) {
            String name = AppConfigure.getUserName();
            String password = AppConfigure.getUserPassword();
            LogUtils.i("-----开始登录,name:" + name);
            loginPhoneEdt.setText(account);
            loginPasswordEdt.setText(password);
            startLogin(name, password);
        }
    }

    @Override
    public void initPresenter() {

    }

    //设置点击事件
    @OnClick({R.id.loginBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.loginBtn:
                account = EditTextUtils.getContent(loginPhoneEdt);
                password = EditTextUtils.getContent(loginPasswordEdt);
                if (EditTextUtils.isEmpty(loginPhoneEdt)) {
                    ToastUtils.showShortToast("账户不能为空");
                } else if (EditTextUtils.isEmpty(loginPasswordEdt)) {
                    ToastUtils.showShortToast("密码不能为空不能为空");
                }
                startLogin(account,password);
                break;

        }
    }

    //初始化标头栏
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("登录");
    }

    /**
     * 开始登录
     */
    private void startLogin(final String account, final String password) {

        showProgress("登录中...");

        BmobQuery<ManagerUser> query = new BmobQuery<ManagerUser>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<ManagerUser>() {
            @Override
            public void done(List<ManagerUser> list, BmobException e) {
                hideProgress();
                if (e == null && list.size() == 1
                        && list.get(0).getPassword().equals(password)) {
                    AppConfigure.saveLoginStatue(true);
                    AppConfigure.saveUserName(account);
                    AppConfigure.saveUserPassword(password);

                    Application.userInstance = list.get(0);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShortToast("登录失败");
                }
            }
        });
    }
}
