package com.jiangtao.shuzicaimanager.model.statistical;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicaimanager.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.helper.SpacesItemDecoration;
import com.jiangtao.shuzicaimanager.model.entry.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.QueryListener;

public class UserCountActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.userRecyclerView)
    RecyclerView userRecyclerView;

    @BindView(R.id.userCountHeader)
    RecyclerViewHeader userHeader;
    //
    @BindView(R.id.user_count_refresh_widget)
    SwipeRefreshLayout userRefreshWidget;
    //用户数
    @BindView(R.id.userCountTxt)
    TextView userCountTxt;
    //适配器
    private QuickAdapter<UserModel> userAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_user_count;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        initData();
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
        setCenterTitle("用户统计");
    }

    //初始化swipe
    private void initSwipeRefresh() {
        userRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        userRefreshWidget.setOnRefreshListener(this);
    }


    //初始化
    private void initRecyclerView() {
        //两列
        userRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        userHeader.attachTo(userRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        userRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        userAdapter = new QuickAdapter<UserModel>(getContext(),
                R.layout.item_user_count_layout, new ArrayList<UserModel>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final UserModel item) {
                helper.setText(R.id.userName, item.getNickName());
                helper.setImageUrl(R.id.userImg, item.getHeadImageUrl());
                helper.setOnClickListener(R.id.user_count_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserCountActivity.this, UserDetailActivity.class);
                        intent.putExtra(UserDetailActivity.Intent_Key_Id, item.getObjectId());
                        startActivity(intent);
                    }
                });
            }
        };
        userRecyclerView.setAdapter(userAdapter);
    }


    /**
     * 获取用户总数
     */
    private void getUserCount() {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.count(BmobUser.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    userCountTxt.setText("总用户：" + count);
                } else {
                    userCountTxt.setText("总用户：0");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 获取用户的信息
     */
    private void getUserData() {
        BmobQuery query = new BmobQuery("_User");
        query.setLimit(100);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                //  LogUtils.i("数据" + jsonArray);
                if (null == jsonArray || jsonArray.length() == 0) {
                    return;
                }
                userAdapter.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        userAdapter.add(new UserModel(object.optString("nickName"),
                                object.optString("headImageUrl")));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        getUserCount();
        getUserData();
    }

}
