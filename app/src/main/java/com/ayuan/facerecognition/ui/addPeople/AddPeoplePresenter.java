package com.ayuan.facerecognition.ui.addPeople;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.ayuan.facerecognition.utils.CameraUtil;

public class AddPeoplePresenter implements AddPeopleLogic.AddPeopleUiRefresh {
    private static final String TAG = "AddPeoplePresenter";
    private final AddPeopleLogic addPeopleLogic;
    private final AddPeopleView addPeopleView;
    private final Handler uiHandler;

    public AddPeoplePresenter(AddPeopleView addPeopleView, AddPeopleLogic addPeopleLogic) {
        this.addPeopleView = addPeopleView;
        this.addPeopleLogic = addPeopleLogic;
        uiHandler = new Handler(Looper.myLooper());
    }

    public void initData() {

    }

    /**
     * 主线程更新
     *
     * @param runnable 回调
     */
    void update(Runnable runnable) {
        uiHandler.post(runnable);
    }

    /**
     * 启动相机并申请权限
     *
     * @param addPeopleActivity 上下文环境
     * @param permissions       需要请求的权限集合
     */
    public void startCameraAndRequestPermission(AddPeopleActivity addPeopleActivity, String[] permissions) {
        addPeopleLogic.requestPermission(addPeopleActivity, permissions);
    }

    /**
     * 权限请求回调
     *
     * @param addPeopleActivity 上下文环境
     * @param requestCode       请求代码
     * @param permissions       允许
     * @param grantResults      授予结果
     */
    public void onRequestPermissionsResult(AddPeopleActivity addPeopleActivity, int requestCode, String[] permissions, int[] grantResults) {
        addPeopleLogic.onRequestPermissionsResult(addPeopleActivity, requestCode, permissions, grantResults, this);
    }

    @Override
    public void startCamera() {
        if (addPeopleView != null) {
            addPeopleView.startCamera();
        }
    }

    /**
     * 回调
     *
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CameraUtil.getImageData(requestCode, resultCode, (Bitmap bmp) -> {
            if (addPeopleView != null) {
                addPeopleView.getCameraBitmap(bmp);
            }
        });
    }
}
