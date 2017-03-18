package com.jiangtao.shuzicaimanager.model.main;

import android.app.IntentService;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.AppConfigure;
import com.jiangtao.shuzicaimanager.Application;
import com.jiangtao.shuzicaimanager.model.entry.ManagerUser;
import com.jiangtao.shuzicaimanager.model.person.LoginActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Nicky on 2017/1/23.
 */
public class SplashIntentService extends IntentService {

    public SplashIntentService() {
        super("Splash");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (AppConfigure.userIsLogin()) {
            String name = AppConfigure.getUserName();
            String password = AppConfigure.getUserPassword();
            LogUtils.i("-----开始登录,name:" + name);
            startLogin(name, password);
        } else {
            Intent intent1 = new Intent(SplashIntentService.this, LoginActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ///////////////////////////////////////////////////////////

    /**
     * 开始登录
     *
     * @param account
     * @param password
     */
    public void startLogin(String account, final String password) {
        BmobQuery<ManagerUser> query = new BmobQuery<ManagerUser>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<ManagerUser>() {
            @Override
            public void done(List<ManagerUser> list, BmobException e) {
                if (e == null && list.size() == 1
                        && list.get(0).getPassword().equals(password)) {
                    Application.userInstance = list.get(0);
                    Intent intent = new Intent(SplashIntentService.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashIntentService.this, LoginActivity.class);
                    startActivity(intent);
                    ToastUtils.showShortToast("登录失败");
                }
            }
        });
    }

}

