package com.jiangtao.shuzicaimanager.model.statistical.user;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.GuessForecastRecord;
import com.jiangtao.shuzicaimanager.model.entry.GuessMantissaRecord;
import com.jiangtao.shuzicaimanager.model.entry.GuessWholeRecord;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

public class GameRecordActivity extends BaseActivityWithToolBar {
    public final static String Intent_Key_Id = "user_id";
    //涨跌
    @BindView(R.id.forecastTxt)
    TextView forecastTxt;
    //尾数
    @BindView(R.id.mantissaTxt)
    TextView mantissaTxt;
    //全数
    @BindView(R.id.wholeTxt)
    TextView wholeTxt;

    @Override
    public int setLayoutId() {
        return R.layout.activity_game_record;
    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void initPresenter() {
        initTitleBar();
        String objectId = getIntent().getStringExtra(Intent_Key_Id);
        getUserData(objectId);
    }

    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("游戏记录");
    }

    /**
     * 获取用户的信息
     *
     * @param objectId
     */
    private void getUserData(String objectId) {
        BmobQuery<GuessForecastRecord> query = new BmobQuery<GuessForecastRecord>();
        query.addWhereEqualTo("userId", objectId);
        query.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    forecastTxt.setText(integer + "次");
                } else {
                    ToastUtils.showLongToast("数据错误");
                }
            }
        });

        BmobQuery<GuessMantissaRecord> query2 = new BmobQuery<GuessMantissaRecord>();
        query2.addWhereEqualTo("userId", objectId);
        query2.count(GuessMantissaRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mantissaTxt.setText(integer + "次");
                } else {
                    ToastUtils.showLongToast("数据错误");
                }
            }
        });

        BmobQuery<GuessWholeRecord> query3 = new BmobQuery<GuessWholeRecord>();
        query3.addWhereEqualTo("userId", objectId);
        query3.count(GuessWholeRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    wholeTxt.setText(integer + "次");
                } else {
                    ToastUtils.showLongToast("数据错误");
                }
            }
        });
    }
}
