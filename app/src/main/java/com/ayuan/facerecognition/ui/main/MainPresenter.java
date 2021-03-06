package com.ayuan.facerecognition.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ayuan.facerecognition.App;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;
import com.ayuan.facerecognition.utils.CameraUtil;

import java.util.List;

public class MainPresenter implements MainLogic.MainUiRefresh {
    private static final String TAG = "MainPresenter";
    private final MainLogic mainLogic;
    private final Handler uiHandler;
    private MainView mainView;

    public MainPresenter(MainView mainView, MainLogic mainLogic) {
        this.mainView = mainView;
        this.mainLogic = mainLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化数据
     */
    public void initData() {
        // 获取人员库列表
        getPeopleLibrary();
    }

    /**
     * 提到UI线程刷新界面
     *
     * @param runnable 刷新回调
     */
    void update(Runnable runnable) {
        uiHandler.post(runnable);
    }

    /**
     * 获取人员库列表
     */
    public void getPeopleLibrary() {
        mainLogic.getPeopleLibrary(this);
    }

    /**
     * @param groupInfosBeans 数据适配器中需要填充的数据对象
     */
    @Override
    public void setPeopleList(List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans) {
        if (mainView != null) {
            update(new Runnable() {
                @Override
                public void run() {
                    mainView.setPeopleList(groupInfosBeans);
                }
            });
        }
    }

    @Override
    public void refreshPeopleList() {
        if (mainView != null) {
            update(new Runnable() {
                @Override
                public void run() {
                    mainView.refreshPeopleList();
                }
            });
        }
    }

    @Override
    public void showDialog() {
        update(new Runnable() {
            @Override
            public void run() {
                mainLogic.showDialog(MainPresenter.this);
            }
        });
    }

    /**
     * 列表的点击事件
     *
     * @param item 获取到的条目
     */
    public void onClick(GetPeopleLibraryBean.ResponseBean.GroupInfosBean item) {
        Log.d(TAG, "onClick: -----点击了----" + item.toString());
        // 跳转到人员列表界面
        if (mainView != null) {
            mainView.startActivity(item.getGroupId());
        }
    }

    /**
     * 更新人员库列表
     */
    public void updatePeopleLibrary() {
        mainLogic.updatePeopleLibrary(this);
    }

    /**
     * 添加人员库
     *
     * @param mainActivity 获取上下文环境
     */
    public void addPeopleLibrary(MainActivity mainActivity) {
        mainLogic.addPeopleLibrary(mainActivity, this);
    }

    /**
     * @param message 需要显示的文字
     */
    @Override
    public void showToast(String message) {
        update(() -> App.showToast(message));
    }

    /**
     * 界面返回的回调
     *
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            updatePeopleLibrary();
        } else if (requestCode == CameraUtil.REQUEST_CODE_IMAGE_CAMERA) {
            if (mainView != null) {
                CameraUtil.getImageData(requestCode, resultCode, (Bitmap bmp) -> {
                    mainView.setImageData(bmp);
                    // 进行网络请求识别人脸
                    mainLogic.searchFaces(bmp, this);
                });
            }
        }
    }

    /**
     * 请求必要权限
     *
     * @param mainActivity 上下文
     * @param permissions  权限
     */
    public void requestPermission(MainActivity mainActivity, String[] permissions) {
        mainLogic.requestPermission(mainActivity, permissions);
    }

    /**
     * 申请权限回调
     *
     * @param mainActivity 上线问
     * @param requestCode  请求代码
     * @param permissions  权限
     * @param grantResults 权限允许结果
     */
    public void onRequestPermissionsResult(MainActivity mainActivity, int requestCode, String[] permissions, int[] grantResults) {
        mainLogic.onRequestPermissionsResult(mainActivity, requestCode, permissions, grantResults, this);
    }

    /**
     * 权限申请成功的回调
     */
    @Override
    public void successfulPermissionApplication() {
        if (mainView != null) {
            mainView.startCamera();
        }
    }

    /**
     * 获取上下文环境
     *
     * @return 返回一个activity
     */
    @Override
    public AppCompatActivity getActivity() {
        return mainView.getActivity();
    }
}
