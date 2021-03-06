package com.jiangtao.shuzicaimanager.model.statistical.user;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.WealthDetail;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

public class WealthRecordActivity extends BaseActivityWithToolBar {
    public final static String Intent_Key_Id = "user_id";
    //充值
    @BindView(R.id.countTxt)
    TextView countTxt;

    @Override
    public int setLayoutId() {
        return R.layout.activity_wealth_record;
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
        setCenterTitle("充值记录");
    }

    /**
     * 获取用户的信息
     *
     * @param objectId
     */
    private void getUserData(String objectId) {
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("userId", objectId);
        query.addWhereEqualTo("operationType",WealthDetail.Operation_Type_Recharge);
        query.count(WealthDetail.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    countTxt.setText(integer + "次");
                } else {
                    ToastUtils.showLongToast("数据错误");
                }
            }
        });

    }

}
