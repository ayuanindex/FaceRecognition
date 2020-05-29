package com.ayuan.facerecognition.ui.peoples;

import android.util.Log;

import com.ayuan.facerecognition.network.HttpUtil;
import com.ayuan.facerecognition.tencentCloud.FaceManager;
import com.ayuan.facerecognition.tencentCloud.bean.DeleteGroupResultBean;
import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class PeopleLogic {
    private static final String TAG = "PeopleLogic";
    private List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans = new ArrayList<>();
    private boolean isRefresh = false;

    interface PeopleUiRefresh {

        /**
         * 设置数据适配器
         *
         * @param personInfosBeans 需要展示的数据
         */
        void setPeopleAdapter(List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans);

        /**
         * 刷新人员列表
         */
        void refreshPeopleList();

        /**
         * 显示Toast
         *
         * @param message 需要提示的文字
         */
        void showToast(String message);

        /**
         * 退出当前界面
         */
        void closeActivity();
    }

    /**
     * 获取人员列表
     *
     * @param groupId         人员库ID
     * @param peopleUiRefresh 刷新回调
     */
    public void getPersonList(String groupId, PeopleUiRefresh peopleUiRefresh) {
        FaceManager.getPersonList(groupId, new HttpUtil.Result<PersonListBean>() {
            @Override
            public void getData(PersonListBean personListBean, Call call, Response response) {
                if (personListBean.getResponse().getPersonInfos() != null) {
                    personInfosBeans.clear();
                    personInfosBeans.addAll(personListBean.getResponse().getPersonInfos());

                    if (isRefresh) {
                        peopleUiRefresh.refreshPeopleList();
                    } else {
                        peopleUiRefresh.setPeopleAdapter(personInfosBeans);
                    }
                    Log.d(TAG, "getData: 获取人员列表-----" + personListBean.toString());
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "error: 获取人员列表-----" + e.getMessage());
            }
        });
    }

    /**
     * 更新数据
     *
     * @param groupId         人员库ID
     * @param peopleUiRefresh 回调
     */
    public void updatePeopleList(String groupId, PeopleUiRefresh peopleUiRefresh) {
        isRefresh = true;
        getPersonList(groupId, peopleUiRefresh);
    }

    /**
     * 删除人员库
     *
     * @param groupId         人员库ID
     * @param peopleUiRefresh 刷新回调
     */
    public void deleteGroup(String groupId, PeopleUiRefresh peopleUiRefresh) {
        FaceManager.deleteGroup(groupId, new HttpUtil.Result<DeleteGroupResultBean>() {
            @Override
            public void getData(DeleteGroupResultBean deleteGroupResultBean, Call call, Response response) {
                Log.d(TAG, "getData: 删除人员库----" + deleteGroupResultBean.toString());
                if (deleteGroupResultBean.getResponse().getError() != null) {
                    // 删除失败
                    peopleUiRefresh.showToast(deleteGroupResultBean.getResponse().getError().getMessage());
                } else {
                    // 删除成功,退出界面，并刷新人员库界面列表
                    peopleUiRefresh.closeActivity();
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getData: 删除人员库----" + e.getMessage());
            }
        });
    }
}
