package com.jiangtao.shuzicaimanager.model.person;

import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.basic.utils.EditTextUtils;
import com.jiangtao.shuzicaimanager.model.entry.ManagerUser;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddManagerUserActivity extends BaseActivityWithToolBar {

    //账户
    @BindView(R.id.managerNameEdt)
    EditText managerNameEdt;
    //账户
    @BindView(R.id.managerAccountEdt)
    EditText managerAccountEdt;
    //密码
    @BindView(R.id.managerPasswordEdt)
    EditText managerPasswordEdt;
    //用户名
    private String account;
    //密码
    private String password;
    //名字
    private String name;

    @Override
    public int setLayoutId() {
        return R.layout.activity_add_manager_user;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    //设置点击事件
    @OnClick({R.id.confirmToAdd})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.confirmToAdd:
                startToAdd();
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
        setCenterTitle("添加管理员");
    }


    /**
     * 开始登录
     */
    private void startToAdd() {
        if (EditTextUtils.isEmpty(managerAccountEdt)) {
            ToastUtils.showShortToast("账户不能为空");
            return;
        } else if (EditTextUtils.isEmpty(managerPasswordEdt)) {
            ToastUtils.showShortToast("密码不能为空不能为空");
            return;
        } else if (EditTextUtils.isEmpty(managerNameEdt)) {
            ToastUtils.showShortToast("名字不能为空不能为空");
            return;
        }
        account = EditTextUtils.getContent(managerAccountEdt);
        password = EditTextUtils.getContent(managerPasswordEdt);
        name = EditTextUtils.getContent(managerNameEdt);

        ManagerUser user = new ManagerUser(name, account, password);
        showProgress("添加中...");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideProgress();
                if (e == null) {
                    ToastUtils.showShortToast("添加成功");
                    finish();
                } else {
                    ToastUtils.showShortToast("添加失败");
                }
            }
        });
    }
}
