package com.jiangtao.shuzicaimanager.model.statistical;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.WealthValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


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


    @Override
    public int setLayoutId() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        String objectId = getIntent().getStringExtra(Intent_Key_Id);
        getUserData(objectId);
        getWealthData(objectId);
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
        setCenterTitle("用户详情");
    }

    /**
     * 获取用户的信息
     */
    private void getUserData(String objectId) {
        BmobQuery query = new BmobQuery("_User");
        query.addWhereEqualTo("objectId", objectId);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                //  LogUtils.i("数据" + jsonArray);
                if (null == jsonArray || jsonArray.length() == 0) {
                    return;
                }
                try {
                    JSONObject object = (JSONObject) jsonArray.get(0);
                    Uri uri = Uri.parse(object.optString("headImageUrl"));
                    userDetailImg.setImageURI(uri);
                    userDetailName.setText(object.optString("nickName"));
                    userDetailStatus.setText(object.optString("nickName"));
                    userDetailCity.setText(object.optString("address"));
                    userDetailSex.setText(object.optInt("gender") == 1 ? "男" : "女");
                    userDetailAccount.setText("电话：" + object.optString("mobilePhoneNumber"));
                    String time = object.opt("createdAt").toString();
                    userDetailRegisterTime.setText("注册时间：" + time);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取用户的财富
     */
    private void getWealthData(String objectId) {
        BmobQuery<WealthValue> query = new BmobQuery<WealthValue>();
        query.addWhereEqualTo("userId", objectId);
        query.findObjects(new FindListener<WealthValue>() {
            @Override
            public void done(List<WealthValue> list, BmobException e) {
                //  LogUtils.i("数据" + jsonArray);
                if (null == list || list.size() == 0) {
                    return;
                }
                userDetailGold.setText(list.get(0).getGoldValue() + "");
                userDetailSilver.setText(list.get(0).getSilverValue() + "");
            }
        });
    }
}
