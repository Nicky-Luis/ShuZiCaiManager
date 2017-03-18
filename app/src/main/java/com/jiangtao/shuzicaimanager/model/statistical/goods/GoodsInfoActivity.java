package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.model.entry.Goods;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.goods_info_exchange_count)
    TextView goods_info_exchange_count;

    @OnClick({R.id.shelvesGoods_btn, R.id.goods_info_inventory_add, R.id.goods_info_inventory_sub})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.shelvesGoods_btn:
                break;

            case R.id.goods_info_inventory_add:
                break;

            case R.id.goods_info_inventory_sub:
                break;
        }
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
        goods_info_price.setText(goods.getGoodsPrice()+"金币");
        goods_info_inventory.setText(goods.getInventory()+"件");
        goods_info_exchange_count.setText(goods.getSalesVolume()+"件");
    }
}
