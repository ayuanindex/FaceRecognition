package com.ayuan.facerecognition.ui.main;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.base.BaseLogic;
import com.ayuan.facerecognition.base.BaseUiRefresh;
import com.ayuan.facerecognition.network.HttpUtil;
import com.ayuan.facerecognition.tencentCloud.FaceManager;
import com.ayuan.facerecognition.tencentCloud.bean.CreatePersonResultBean;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;
import com.ayuan.facerecognition.tencentCloud.bean.PersonBaseInfoBean;
import com.ayuan.facerecognition.tencentCloud.bean.SearchPersonResultBean;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainLogic extends BaseLogic {
    private static final String TAG = "MainLogic";
    private boolean isRefresh = false;
    private static List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans = new ArrayList<>();
    private static List<String> groups = new ArrayList<>();
    private PersonBaseInfoBean personBaseInfoBean;
    private Bitmap searchBitmap;

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

        /**
         * 显示对话框
         */
        void showDialog();
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

                    groups.clear();
                    for (GetPeopleLibraryBean.ResponseBean.GroupInfosBean groupInfosBean : groupInfosBeans) {
                        groups.add(groupInfosBean.getGroupId());
                    }

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

    /**
     * 在人员库中搜索人脸
     *
     * @param bmp 包含人脸的图片
     */
    public void searchFaces(Bitmap bmp, MainUiRefresh mainUiRefresh) {
        searchBitmap = bmp;
        FaceManager.searchFaces(bmp, groups, new HttpUtil.Result<SearchPersonResultBean>() {
            @Override
            public void getData(SearchPersonResultBean searchPersonResultBean, Call call, Response response) {
                if (searchPersonResultBean.getResponse().getError() != null) {
                    mainUiRefresh.showToast(searchPersonResultBean.getResponse().getError().getMessage());
                } else {
                    // 将匹配到的人脸结果按照可信度排序
                    ArrayList<SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean> candidatesBeans = new ArrayList<>();
                    for (SearchPersonResultBean.ResponseBean.ResultsBean result : searchPersonResultBean.getResponse().getResults()) {
                        candidatesBeans.addAll(result.getCandidates());
                    }

                    Collections.sort(candidatesBeans, new Comparator<SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean>() {
                        @Override
                        public int compare(SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean o1, SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean o2) {
                            return (int) ((Double.parseDouble(o2.getScore()) * Integer.MAX_VALUE) - (Double.parseDouble(o1.getScore()) * Integer.MAX_VALUE));
                        }
                    });

                    // 根据识别到的信息获取识别到的人的信息
                    if (Double.parseDouble(candidatesBeans.get(0).getScore()) > 90) {
                        getPersonBaseInfo(candidatesBeans.get(0).getPersonId(), mainUiRefresh);
                    } else {
                        mainUiRefresh.showToast("该人脸未录入");
                    }
                }
                Log.d(TAG, "getData: 搜索人脸信息-----" + searchPersonResultBean.toString());
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getData: 搜索人脸信息-----" + e.getMessage());
            }
        });
    }

    /**
     * 获取人员基础信息
     *
     * @param personId      人员ID
     * @param mainUiRefresh 回调
     */
    private void getPersonBaseInfo(String personId, MainUiRefresh mainUiRefresh) {
        FaceManager.getPersonBaseInfo(personId, new HttpUtil.Result<PersonBaseInfoBean>() {
            @Override
            public void getData(PersonBaseInfoBean personBaseInfoBean, Call call, Response response) {
                if (personBaseInfoBean.getResponse().getError() != null) {
                    mainUiRefresh.showToast(personBaseInfoBean.getResponse().getError().getMessage());
                } else {
                    MainLogic.this.personBaseInfoBean = personBaseInfoBean;
                    // 弹出对话框显示人员信息
                    mainUiRefresh.showDialog();
                }
                Log.d(TAG, "getData: 获取人员基本信息------------" + personBaseInfoBean.toString());
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getData: 获取人员基本信息------------" + e.getMessage());
            }
        });
    }

    /**
     * 显示对话框
     *
     * @param mainUiRefresh 回调
     */
    public void showDialog(MainUiRefresh mainUiRefresh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainUiRefresh.getActivity());
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(mainUiRefresh.getActivity(), R.layout.dialog_result_person_info, null);
        alertDialog.setView(inflate);

        DialogViewHolder dialogViewHolder = new DialogViewHolder(inflate, alertDialog);
        dialogViewHolder.init();
        dialogViewHolder.setName(personBaseInfoBean.getResponse().getPersonName());
        dialogViewHolder.setImage(searchBitmap);

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
