package com.ayuan.facerecognition.ui.peoples;

import android.os.Handler;
import android.os.Looper;

import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;

import java.util.List;

public class PeoplePresenter implements PeopleLogic.PeopleUiRefresh {

    private final PeopleView peopleView;
    private final PeopleLogic peopleLogic;
    private final Handler uiHandler;

    public PeoplePresenter(PeopleView peopleView, PeopleLogic peopleLogic) {
        this.peopleView = peopleView;
        this.peopleLogic = peopleLogic;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public void initData() {
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
     *
     * @param groupId 人员库ID
     */
    public void getPersonList(String groupId) {
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
     *
     * @param groupId 人员库ID
     */
    public void updatePeopleList(String groupId) {
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
}
