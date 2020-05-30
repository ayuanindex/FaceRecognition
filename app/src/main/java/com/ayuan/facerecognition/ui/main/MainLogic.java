package com.ayuan.facerecognition.ui.main;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.base.BaseUiRefresh;
import com.ayuan.facerecognition.network.HttpUtil;
import com.ayuan.facerecognition.tencentCloud.FaceManager;
import com.ayuan.facerecognition.tencentCloud.bean.CreatePersonResultBean;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainLogic {
    private static final String TAG = "MainLogic";
    private boolean isRefresh = false;
    private static List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans = new ArrayList<>();

    interface MainUiRefresh extends BaseUiRefresh {

        /**
         * 设置数据适配器
         *
         * @param groupInfosBeans 需要显示的数据集合
         */
        void setPeopleList(List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans);

        /**
         * 刷新列表
         */
        void refreshPeopleList();
    }

    /**
     * 获取人员库列表
     *
     * @param mainUiRefresh 回调
     */
    public void getPeopleLibrary(MainUiRefresh mainUiRefresh) {
        FaceManager.getPeopleLibrary(new HttpUtil.Result<GetPeopleLibraryBean>() {
            @Override
            public void getData(GetPeopleLibraryBean getPeopleLibraryBean, Call call, Response response) {
                if (getPeopleLibraryBean.getResponse().getGroupInfos() != null) {
                    groupInfosBeans.clear();
                    groupInfosBeans.addAll(getPeopleLibraryBean.getResponse().getGroupInfos());

                    if (isRefresh) {
                        isRefresh = false;
                        mainUiRefresh.refreshPeopleList();
                    } else {
                        mainUiRefresh.setPeopleList(groupInfosBeans);
                    }

                    Log.d(TAG, "getData: 获取人员库信息成功----------" + getPeopleLibraryBean.toString());
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "error: 获取人员库信息发生错误------" + e.getMessage());
            }
        });
    }

    /**
     * 更新人员库列表
     *
     * @param mainUiRefresh 回调
     */
    public void updatePeopleLibrary(MainUiRefresh mainUiRefresh) {
        isRefresh = true;
        getPeopleLibrary(mainUiRefresh);
    }

    /**
     * 添加人员库
     *
     * @param mainActivity 上下文环境
     */
    public void addPeopleLibrary(MainActivity mainActivity, MainUiRefresh mainUiRefresh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(mainActivity, R.layout.dialog_add_peoplelibrary, null);
        alertDialog.setView(inflate);
        ViewHolder holder = new ViewHolder(inflate);

        holder.cardOk.setOnClickListener((View v) -> {
            boolean flag = true;

            String name = holder.etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                holder.etName.setHint("请输入人员库名称");
                flag = false;
            }

            String id = holder.etId.getText().toString().trim();
            if (TextUtils.isEmpty(id)) {
                holder.etId.setHint("请输入人员库ID");
                flag = false;
            }

            if (!flag) {
                return;
            }

            // 开始创建人员库
            FaceManager.createGroup(name, id, new HttpUtil.Result<CreatePersonResultBean>() {
                @Override
                public void getData(CreatePersonResultBean createPersonResultBean, Call call, Response response) {
                    Log.d(TAG, "getData: 创建人员库成功-----" + createPersonResultBean.toString());
                    if (createPersonResultBean.getResponse().getError() != null) {
                        mainUiRefresh.showToast(createPersonResultBean.getResponse().getError().getMessage());
                    } else {
                        // 创建成功，更新列表
                        updatePeopleLibrary(mainUiRefresh);
                        alertDialog.dismiss();
                    }
                }

                @Override
                public void error(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "error: 创建人员库出现错误----" + e.getMessage());
                }
            });
        });

        holder.cardCancel.setOnClickListener((View v) -> {
            alertDialog.dismiss();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static
    class ViewHolder {
        public View rootView;
        public TextInputEditText etName;
        public TextInputEditText etId;
        public CardView cardCancel;
        public CardView cardOk;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.etName = rootView.findViewById(R.id.etName);
            this.etId = rootView.findViewById(R.id.etId);
            this.cardCancel = rootView.findViewById(R.id.cardCancel);
            this.cardOk = rootView.findViewById(R.id.cardOk);
        }

    }
}
