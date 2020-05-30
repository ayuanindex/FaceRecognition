package com.ayuan.facerecognition.ui.addPeople;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.ayuan.facerecognition.App;
import com.ayuan.facerecognition.utils.CameraUtil;

public class AddPeoplePresenter implements AddPeopleLogic.AddPeopleUiRefresh {
    private static final String TAG = "AddPeoplePresenter";
    private final AddPeopleLogic addPeopleLogic;
    private final AddPeopleView addPeopleView;
    private final Handler uiHandler;
    private final String groupId;

    public AddPeoplePresenter(AddPeopleView addPeopleView, AddPeopleLogic addPeopleLogic, String groupId) {
        this.addPeopleView = addPeopleView;
        this.addPeopleLogic = addPeopleLogic;
        this.groupId = groupId;

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

    /**
     * 打开相机
     */
    public void startCamera() {
        if (addPeopleView != null) {
            addPeopleView.startCamera();
        }
    }

    /**
     * 显示Toast
     *
     * @param message 需要显示的文字
     */
    @Override
    public void showToast(String message) {
        update(() -> App.showToast(message));
    }

    /**
     * 关闭当前界面
     */
    @Override
    public void closeActivity() {
        if (addPeopleView != null) {
            update(new Runnable() {
                @Override
                public void run() {
                    addPeopleView.closeActivity();
                }
            });
        }
    }

    @Override
    public void successfulPermissionApplication() {
        startCamera();
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

    /**
     * 提交数据
     *
     * @param faceBitmap 包含人脸的图片
     * @param name       人员名称
     * @param id         人员ID
     * @param isMan      男性和女性的标示符
     */
    public void submit(Bitmap faceBitmap, String name, String id, int isMan) {
        addPeopleLogic.submit(faceBitmap, name, id, isMan, groupId, this);
    }
}
