package com.ayuan.facerecognition.ui.main;

import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;

import java.util.List;

public interface MainView {
    /**
     * 填充集合
     *
     * @param groupInfosBeans 数据适配器中需要填充的数据对象
     */
    void setPeopleList(List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans);

    void refreshPeopleList();

    /**
     * 界面跳转
     *
     * @param groupId 人员库ID
     */
    void startActivity(String groupId);
}
