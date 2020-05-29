package com.ayuan.facerecognition.ui.peoples;

import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;

import java.util.List;

public interface PeopleView {
    /**
     * 设置数据适配器
     *
     * @param personInfosBeans 需要设置的数据集合
     */
    void setPeopleAdapter(List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans);

    /**
     * 刷新列表
     */
    void refreshPeopleList();

    /**
     * 退出界面
     */
    void closeActivity();
}
