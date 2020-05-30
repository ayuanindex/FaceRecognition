package com.ayuan.facerecognition.base;

public interface BaseUiRefresh {

    /**
     * 显示Toast
     *
     * @param message 需要显示的文字
     */
    void showToast(String message);

    /**
     * 关闭当前界面
     */
    void closeActivity();

    /**
     * 权限申请成功回调
     */
    void successfulPermissionApplication();
}
