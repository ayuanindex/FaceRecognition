package com.ayuan.facerecognition.ui.main;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ayuan.facerecognition.App;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;

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
}
