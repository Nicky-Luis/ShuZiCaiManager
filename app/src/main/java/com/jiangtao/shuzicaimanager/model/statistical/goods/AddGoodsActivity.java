package com.jiangtao.shuzicaimanager.model.statistical.goods;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicaimanager.R;
import com.jiangtao.shuzicaimanager.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicaimanager.basic.utils.EditTextUtils;
import com.jiangtao.shuzicaimanager.common.event_message.SelectGalleryPhotoMsg;
import com.jiangtao.shuzicaimanager.common.photo_gallery.imageloader.GalleyActivity;
import com.jiangtao.shuzicaimanager.model.entry.Goods;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 添加商品
 */
public class AddGoodsActivity extends BaseActivityWithToolBar {
    //
    @BindView(R.id.goods_add_img)
    SimpleDraweeView goods_add_img;
    //
    @BindView(R.id.goods_add_name)
    EditText goods_add_name;
    //
    @BindView(R.id.goods_add_inventory)
    EditText goods_add_inventory;
    //
    @BindView(R.id.goods_add_price)
    EditText goods_add_price;
    //商品描述
    @BindView(R.id.goods_add_detail)
    EditText goods_add_detail;
    //头像路径
    private String filePath;

    @OnClick({R.id.saveGoods, R.id.goods_add_img})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.saveGoods:
                startUploadImg();
                break;

            case R.id.goods_add_img:
                selectPhotos();
                break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_add_goods;
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
        setLeftImage(R.mipmap.btn_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("添加商品");
    }


    /**
     * 选择照片
     */
    private void selectPhotos() {
        // 相册
        Intent intentAlbum = new Intent(AddGoodsActivity.this, GalleyActivity.class);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_SELECTED_COUNT, 0);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_ONE, true);
        startActivity(intentAlbum);
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(SelectGalleryPhotoMsg event) {
        if (null == event) {
            return;
        }
        List<String> photoList = event.getPhotoList();
        if (null != photoList && photoList.size() > 0) {
            filePath = photoList.get(0);
            // RegisterSetInfoPresenter.replayPlace(filePath);

            Uri uri = Uri.parse("file://" + filePath);
            LogUtils.i("选择的图片路径：" + uri.toString());
            goods_add_img.setImageURI(uri);
        }
    }

    private void startUploadImg() {
        if (EditTextUtils.isEmpty(goods_add_name) ||
                EditTextUtils.isEmpty(goods_add_inventory) ||
                EditTextUtils.isEmpty(goods_add_price)) {
            ToastUtils.showShortToast("请把信息输入完整");
            return;
        }
        if (filePath == null || filePath.length() <= 1) {
            ToastUtils.showShortToast("请选取商品图片");
            return;
        }
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        showProgress("上传信息中...");
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    startUpLoadInfo(bmobFile.getFileUrl());
                } else {
                    hideProgress();
                    ToastUtils.showShortToast("上传图片失败");
                    LogUtils.i("上传文件失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 上传商品信息
     *
     * @param imageUrl
     */
    private void startUpLoadInfo(String imageUrl) {
        if (null == imageUrl) {
            ToastUtils.showShortToast("图片信息有误");
            return;
        }
        Goods goods = new Goods(
                EditTextUtils.getContent(goods_add_name),
                EditTextUtils.getContent(goods_add_detail),
                Float.valueOf(EditTextUtils.getContent(goods_add_price)),
                imageUrl,
                Integer.valueOf(EditTextUtils.getContent(goods_add_inventory)),
                0,
                1);
        //保存信息
        goods.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideProgress();
                if (e == null) {
                    ToastUtils.showShortToast("商品添加成功");
                    finish();
                } else {
                    ToastUtils.showShortToast("上传图片失败");
                    LogUtils.i("上传文件失败：" + e.getMessage());
                }
            }
        });
    }
}
