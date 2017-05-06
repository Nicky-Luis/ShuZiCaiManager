package com.jiangtao.shuzicaimanager;


import android.content.Context;

import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.blankj.utilcode.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.jiangtao.shuzicaimanager.basic.app.BasicApp;
import com.jiangtao.shuzicaimanager.basic.network.APIInteractive;
import com.jiangtao.shuzicaimanager.basic.utils.AppCrashUtils;
import com.jiangtao.shuzicaimanager.model.entry.Config;
import com.jiangtao.shuzicaimanager.model.entry.ManagerUser;

import cn.bmob.v3.Bmob;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class Application extends BasicApp {

    //全局上下文
    public static Context APPContext;
    //线程池
    public static ThreadPoolUtils AppThreadPool;
    //管理类用户实例
    public static ManagerUser userInstance;
    //配置
    public static Config appConfig;

    //获取比目所有APP信息
    public static String getUserURl() {
        return getStringRes(Application.APPContext, R.string.Bmob_URL);
    }

    @Override
    public String getApplicationName() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public String getApplicationNameId() {
        return getResources().getString(R.string.app_name_id);
    }

    @Override
    public void onApplicationInit() {
        Application.APPContext = this;
        //初始化retrofit
        APIInteractive.initRetrofit();
        //utils初始化
        Utils.init(this);
        //初始化全局异常处理
        AppCrashUtils.getInstance().init(this);
        //初始化Fresco,防止滑动卡顿
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true).build();
        Fresco.initialize(this, config);
        //初始化线程池
        AppThreadPool = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 5);
        //初始化bmob SDK
        initBmob();
    }

    private void initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "cd89b563ca70dfd60befb89fa9ad6e42");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }
}
