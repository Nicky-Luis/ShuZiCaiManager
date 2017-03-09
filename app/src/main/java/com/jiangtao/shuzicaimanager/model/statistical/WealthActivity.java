package com.jiangtao.shuzicaimanager.model.statistical;

import android.content.Intent;
import android.view.View;

import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;

public class WealthActivity extends BaseActivityWithToolBar{

    @Override
    public int setLayoutId() {
        return R.layout.activity_wealth;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("尾数预测");
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //intent.setClass(GuessMantissaActivity.this, GuessMantissaDetailActivity.class);
                //startActivity(intent);
            }
        });
    }
}
