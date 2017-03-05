package com.jiangtao.shuzicaimanager.model.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

/**
 * this is splash activity
 */
public class SplashActivity extends AppCompatActivity {

    //启动的时间
    private final static int LunchTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initApp();
        SystemClock.sleep(LunchTime);
        //跳转
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化APP
     */
    private void initApp(){
        Intent intent = new Intent(this, SplashIntentService.class);
        startService(intent);
    }
}