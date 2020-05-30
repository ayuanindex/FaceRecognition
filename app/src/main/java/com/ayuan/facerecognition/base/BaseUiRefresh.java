package com.ayuan.facerecognition.base;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public interface BaseUiRefresh {
    String TAG = "未实现方法";

    /**
     * 显示Toast
     *
     * @param message 需要显示的文字
     */
    default void showToast(String message) {
        Log.d(TAG, "showToast: ");
    }

    /**
     * 关闭当前界面
     */
    default void closeActivity() {
        Log.d(TAG, "closeActivity: ");
    }

    /**
     * 权限申请成功回调
     */
    default void successfulPermissionApplication() {
        Log.d(TAG, "successfulPermissionApplication: ");
    }

    /**
     * 获取上下文环境
     *
     * @return 上下文环境
     */
    default AppCompatActivity getActivity() {
        Log.d(TAG, "getActivity: ");
        return null;
    }

}
