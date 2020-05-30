package com.ayuan.facerecognition.ui.addPeople;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.ayuan.facerecognition.base.BaseLogic;
import com.ayuan.facerecognition.base.BaseUiRefresh;
import com.ayuan.facerecognition.network.HttpUtil;
import com.ayuan.facerecognition.tencentCloud.FaceManager;
import com.ayuan.facerecognition.tencentCloud.bean.CreatePersonResultBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AddPeopleLogic extends BaseLogic {
    private static final String TAG = "AddPeopleLogic";

    interface AddPeopleUiRefresh extends BaseUiRefresh {

    }

    /**
     * 请求必要的权限
     *
     * @param addPeopleActivity 上下文环境
     * @param permissions       需要请求的权限集合
     */
    /*public void requestPermission(AddPeopleActivity addPeopleActivity, String[] permissions) {
        addPeopleActivity.requestPermissions(permissions, 100);
    }*/

    /**
     * 权限请求回调
     *
     * @param addPeopleActivity 上下文环境
     * @param requestCode       请求代码
     * @param permissions       允许
     * @param grantResults      授予结果
     */
    /*public void onRequestPermissionsResult(AddPeopleActivity addPeopleActivity, int requestCode, String[] permissions, int[] grantResults, AddPeopleUiRefresh addPeopleUiRefresh) {
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
    }*/

    /**
     * 提交数据
     *
     * @param faceBitmap         包含人脸的图片
     * @param name               人员名称
     * @param personId           人员ID
     * @param isMan              男性和女性的标示符
     * @param groupId            人员库ID
     * @param addPeopleUiRefresh 界面刷新回调
     */
    public void submit(Bitmap faceBitmap, String name, String personId, int isMan, String groupId, AddPeopleUiRefresh addPeopleUiRefresh) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(personId) || faceBitmap == null) {
            addPeopleUiRefresh.showToast("请将数据填写完整");
            return;
        }

        FaceManager.createPerson(faceBitmap, name, groupId, personId, isMan, new HttpUtil.Result<CreatePersonResultBean>() {
            @Override
            public void getData(CreatePersonResultBean createPersonResultBean, Call call, Response response) {
                CreatePersonResultBean.ResponseBean body = createPersonResultBean.getResponse();

                if (body.getError() != null) {
                    addPeopleUiRefresh.showToast(body.getError().getMessage());
                } else {
                    addPeopleUiRefresh.showToast("添加成功");
                    addPeopleUiRefresh.closeActivity();
                }

                Log.d(TAG, "getData: 创建人员成功-------------" + createPersonResultBean.toString());
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "error: 创建人员出现问题----------" + e.getMessage());
            }
        });
    }
}
