package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.Goods;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsInfoActivity extends BaseActivityWithToolBar {

    public final static String Intent_Key = "Goods";
    private Goods goods;
    //
    @BindView(R.id.goods_info_img)
    SimpleDraweeView goods_info_img;
    //
    @BindView(R.id.goods_info_name)
    TextView goods_info_name;
    //
    @BindView(R.id.goods_info_price)
    TextView goods_info_price;
    //
    @BindView(R.id.goods_info_inventory)
    TextView goods_info_inventory;
    //
    @BindView(R.id.goods_info_type)
    TextView goods_info_type;
    ///
    @BindView(R.id.goods_info_detail)
    TextView goods_info_detail;

    @OnClick({R.id.delete_Goods_btn, R.id.goods_info_inventory_add, R.id.goods_info_inventory_sub})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.delete_Goods_btn:
                startDeleteGood();
                break;

            case R.id.goods_info_inventory_add:
                startAdd();
                break;

            case R.id.goods_info_inventory_sub:
                startSub();
                break;
        }
    }

    //减少库存
    private void startSub() {
        if (null == goods) {
            return;
        }
        goods.setInventory(goods.getInventory() - 1);
        goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    goods_info_inventory.setText(goods.getInventory() + "件");
                } else {
                    LogUtils.e("减少失败：" + e);
                }
            }
        });
    }

    //增加库存
    private void startAdd() {
        if (null == goods) {
            return;
        }
        goods.setInventory(goods.getInventory() + 1);
        goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    goods_info_inventory.setText(goods.getInventory() + "件");
                } else {
                    LogUtils.e("增加失败：" + e);
                }
            }
        });
    }

    //开始删除商品
    private void startDeleteGood() {
        new AlertDialog.Builder(GoodsInfoActivity.this)
                .setMessage("确定删除商品吗？")
                .setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null == goods) {
                            dialog.dismiss();
                            return;
                        }
                        goods.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("删除成功");
                                    finish();
                                } else {
                                    ToastUtils.showShortToast("删除失败");
                                    LogUtils.e("删除失败：" + e);
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_goods_info;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        goods = (Goods) getIntent().getSerializableExtra(Intent_Key);
        initData();
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
        setCenterTitle("商品详情");
    }


    private void initData() {
        goods_info_img.setImageURI(Uri.parse(goods.getGoodsImgUrl()));
        goods_info_name.setText(goods.getGoodsName());
        goods_info_price.setText(goods.getGoodsPrice() + "金币");
        goods_info_inventory.setText(goods.getInventory() + "件");
        String type = "手机充值卡";
        if (goods.getType() == 0) {
            type = "实物商品";
        } else if (goods.getType() == 2) {
            type = "Q币充值卡";
        }
        goods_info_type.setText(type);
        goods_info_detail.setText(goods.getGoodsDetail());
    }
}
