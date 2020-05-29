package com.ayuan.facerecognition.ui.peoples;

import android.os.Handler;
import android.os.Looper;

import com.ayuan.facerecognition.App;
import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;

import java.util.List;

public class PeoplePresenter implements PeopleLogic.PeopleUiRefresh {

    private final PeopleView peopleView;
    private final PeopleLogic peopleLogic;
    private final Handler uiHandler;
    private final String groupId;

    public PeoplePresenter(PeopleView peopleView, PeopleLogic peopleLogic, String groupId) {
        this.peopleView = peopleView;
        this.peopleLogic = peopleLogic;
        this.groupId = groupId;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public void initData() {
        getPersonList();
    }

    /**
     * 在主线程中刷新
     *
     * @param runnable 回调
     */
    void update(Runnable runnable) {
        uiHandler.post(runnable);
    }

    /**
     * 获取人员列表
     */
    public void getPersonList() {
        peopleLogic.getPersonList(groupId, this);
    }

    /**
     * 设置数据适配器
     *
     * @param personInfosBeans 需要展示的数据
     */
    @Override
    public void setPeopleAdapter(List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans) {
        if (peopleView != null) {
            update(() -> peopleView.setPeopleAdapter(personInfosBeans));
        }
    }

    /**
     * 更新数据
     */
    public void updatePeopleList() {
        peopleLogic.updatePeopleList(groupId, this);
    }

    /**
     * 刷新列表
     */
    @Override
    public void refreshPeopleList() {
        if (peopleView != null) {
            update(new Runnable() {
                @Override
                public void run() {
                    peopleView.refreshPeopleList();
                }
            });
        }
    }

    @Override
    public void showToast(String message) {
        update(() -> App.showToast(message));
    }

    /**
     * 退出界面
     */
    @Override
    public void closeActivity() {
        if (peopleView != null) {
            update(new Runnable() {
                @Override
                public void run() {
                    peopleView.closeActivity();
                }
            });
        }
    }

    /**
     * 删除人员库
     */
    public void deleteGroup() {
        peopleLogic.deleteGroup(groupId, this);
    }
}
