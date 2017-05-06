package com.jiangtao.shuzicaimanager.model.statistical.user;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry._User;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class UserDetailActivity extends BaseActivityWithToolBar {

    public final static String Intent_Key_Id = "user_id";
    //头像
    @BindView(R.id.userDetailImg)
    SimpleDraweeView userDetailImg;
    //名字
    @BindView(R.id.userDetailName)
    TextView userDetailName;
    @BindView(R.id.userDetailStatus)
    TextView userDetailStatus;
    //
    @BindView(R.id.userDetailCity)
    TextView userDetailCity;
    //
    @BindView(R.id.userDetailSex)
    TextView userDetailSex;
    //
    @BindView(R.id.userDetailRegisterTime)
    TextView userDetailRegisterTime;
    //
    @BindView(R.id.userDetailAccount)
    TextView userDetailAccount;
    //
    @BindView(R.id.userDetailGold)
    TextView userDetailGold;
    //
    @BindView(R.id.userDetailSilver)
    TextView userDetailSilver;


    //设置点击事件
    @OnClick({R.id.ly_game_record, R.id.ly_wealth_record,
            R.id.ly_exchange_record, R.id.ly_invite_record})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.ly_game_record: {
                Intent intent = new Intent(UserDetailActivity.this, GameRecordActivity.class);
                intent.putExtra(GameRecordActivity.Intent_Key_Id, objectId);
                startActivity(intent);
            }
            break;

            case R.id.ly_wealth_record: {
                Intent intent = new Intent(UserDetailActivity.this, WealthRecordActivity.class);
                intent.putExtra(WealthRecordActivity.Intent_Key_Id, objectId);
                startActivity(intent);
            }
            break;

            case R.id.ly_exchange_record: {
                Intent intent = new Intent(UserDetailActivity.this, ExchangeRecordActivity.class);
                intent.putExtra(ExchangeRecordActivity.Intent_Key_Id, objectId);
                startActivity(intent);
            }
            break;

            case R.id.ly_invite_record: {
                Intent intent = new Intent(UserDetailActivity.this, InviteRecordActivity.class);
                intent.putExtra(InviteRecordActivity.Intent_Key_Id, objectId);
                startActivity(intent);
            }
            break;

        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_user_detail;
    }

    //objectId
    private String objectId;

    @Override
    protected void onInitialize() {
        initTitleBar();
        objectId = getIntent().getStringExtra(Intent_Key_Id);
        getUserData(objectId);
    }

    @Override
    public void initPresenter() {

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
        setCenterTitle("用户详情");
    }

    /**
     * 获取用户的信息
     *
     * @param objectId
     */
    private void getUserData(String objectId) {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    _User user = list.get(0);
                    Uri uri = Uri.parse(user.getHeadImageUrl());
                    userDetailImg.setImageURI(uri);
                    userDetailName.setText(user.getNickName());
                    userDetailStatus.setText(user.getNickName());
                    userDetailCity.setText(user.getAddress());
                    userDetailSex.setText(user.getGender() == 1 ? "男" : "女");
                    userDetailAccount.setText("电话：" + user.getMobilePhoneNumber());
                    userDetailRegisterTime.setText("注册时间：" + user.getCreatedAt());
                    userDetailGold.setText(user.getGoldValue() + "");
                    userDetailSilver.setText(user.getSilverValue() + "");
                } else {
                    ToastUtils.showLongToast("数据错误");
                }
            }
        });
    }
}
