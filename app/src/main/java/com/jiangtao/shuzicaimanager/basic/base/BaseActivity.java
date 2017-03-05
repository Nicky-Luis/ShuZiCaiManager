package com.jiangtao.shuzicaimanager.basic.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jiangtao.shuzicaimanager.basic.manager.ActivityManager;
import com.jiangtao.shuzicaimanager.basic.widget.CustomConfirmDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private ProgressDialog mProgressDialog;
    protected FragmentManager fragmentManager;
    //对activity的引用
    private WeakReference<Activity> mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //对当前activity的软引用防止内存泄露
        this.mActivity = new WeakReference<Activity>(this);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActivityManager.getAppManager().addActivity(mActivity.get());
        int id = setLayoutId();
        if (0 == id) {
            new Exception("Please return the layout id in setLayoutId method,as simple as R" +
                    ".layout.cr_news_fragment_layout").printStackTrace();
        } else {
            // layout注入
            View rootView = LayoutInflater.from(this).inflate(id, null);
            setContentView(rootView);
            loadLayout(rootView);
            //检测是否有内存泄露
            // RefWatcher refWatcher = BasicApp.getRefWatcher(this);
            // refWatcher.watch(this);
        }
        //初始化BUtterKnife框架
        ButterKnife.bind(this);
        //注册event bus
        EventBus.getDefault().register(this);

        initPresenter();
        onInitialize();
    }

    @Override
    protected void onDestroy() {
        //注册event bus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 重写方法设置layoutID
     *
     * @return int
     */
    public abstract int setLayoutId();

    protected abstract void loadLayout(View view);

    protected abstract void onInitialize();

    public abstract void initPresenter();

    @Override
    public void finish() {
        super.finish();
    }


    /**
     * 显示单选对话框
     *
     * @param title           标题
     * @param message         提示信息
     * @param strings         选项数组
     * @param checkedItem     默认选中
     * @param onClickListener 点击事件的监听
     */
    public void showRadioButtonDialog(String title, String message, String[] strings,
                                      int checkedItem, DialogInterface.OnClickListener
                                              onClickListener) {
        if (null != mActivity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.get());
            builder.setTitle(title);
            if (!TextUtils.isEmpty(message)) {
                builder.setMessage(message);
            }
            builder.setSingleChoiceItems(strings, checkedItem, onClickListener);
            builder.create();
            builder.show();
        }
    }

    /**
     * 显示单选对话框
     *
     * @param title           标题
     * @param strings         选项数组
     * @param onClickListener 点击事件的监听
     */
    public void showRadioButtonDialog(String title, String[] strings, DialogInterface
            .OnClickListener onClickListener) {
        showRadioButtonDialog(title, null, strings, 0, onClickListener);
    }

    /**
     * 弹出自定义对话框
     */
    public void showConfirmDialog(String title, View.OnClickListener positiveListener) {
        if (null != mActivity) {
            CustomConfirmDialog confirmDialog = new CustomConfirmDialog(mActivity.get()
                    , title, positiveListener);
            confirmDialog.show();
        }
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (null != mActivity) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(mActivity.get());
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(flag);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage(message);
            }

            mProgressDialog.show();
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String msg) {
        if (null != mActivity) {
            if (!isFinishing()) {
                Toast.makeText(mActivity.get(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Context getContext() {
        return mActivity.get();
    }

    @Override
    public void close() {
        finish();
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

    //--------------------------Fragment相关--------------------------//

    /**
     * 获取Fragment管理器
     *
     * @return
     */
    public FragmentManager getBaseFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        return fragmentManager;
    }

    /**
     * 获取Fragment事物管理
     *
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        return getBaseFragmentManager().beginTransaction();
    }

    /**
     * 替换一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void replaceFragment(int res, BaseFragment fragment) {
        replaceFragment(res, fragment, false);
    }

    /**
     * 替换一个Fragment并设置是否加入回退栈
     *
     * @param res
     * @param fragment
     * @param isAddToBackStack
     */
    public void replaceFragment(int res, BaseFragment fragment, boolean
            isAddToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.replace(res, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    /**
     * 添加一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void addFragment(int res, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.add(res, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 移除一个Fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 显示一个Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 隐藏一个Fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (!fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    //--------------------------Fragment相关end--------------------------//
}
