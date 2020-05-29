package com.ayuan.facerecognition.ui.addPeople;

import android.util.Log;

import com.ayuan.facerecognition.utils.CameraUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class AddPeopleLogic {
    private static final String TAG = "AddPeopleLogic";

    interface AddPeopleUiRefresh {


        /**
         * 开启相机
         */
        void startCamera();
    }

    /**
     * 请求必要的权限
     *
     * @param addPeopleActivity 上下文环境
     * @param permissions       需要请求的权限集合
     */
    public void requestPermission(AddPeopleActivity addPeopleActivity, String[] permissions) {
        addPeopleActivity.requestPermissions(permissions, 100);
    }

    /**
     * 权限请求回调
     *
     * @param addPeopleActivity 上下文环境
     * @param requestCode       请求代码
     * @param permissions       允许
     * @param grantResults      授予结果
     */
    public void onRequestPermissionsResult(AddPeopleActivity addPeopleActivity, int requestCode, String[] permissions, int[] grantResults, AddPeopleUiRefresh addPeopleUiRefresh) {
        int sum = 0;
        for (int grantResult : grantResults) {
            sum += grantResult;
        }

        // 已同意当前请求的所有权限
        if (sum == 0) {
            CameraUtil.startCamera(addPeopleActivity);
            return;
        }

        // 记录被拒绝的下标
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != 0) {
                list.add(i);
            }
        }

        // 通过被拒绝的下标获取对应的权限
        ArrayList<String> strings = new ArrayList<>();
        for (Integer integer : list) {
            strings.add(permissions[integer]);
        }

        if (grantResults.length == 1 && grantResults[0] == 0) {
            addPeopleUiRefresh.startCamera();
            return;
        }

        // 重新申请权限
        requestPermission(addPeopleActivity, strings.toArray(new String[]{}));
        Log.d(TAG, "onRequestPermissionsResult: requestCode" + requestCode);
        Log.d(TAG, "onRequestPermissionsResult: permissions" + Arrays.toString(permissions));
        Log.d(TAG, "onRequestPermissionsResult: grantResults" + Arrays.toString(grantResults));
    }
}
