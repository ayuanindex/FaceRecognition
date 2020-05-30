package com.ayuan.facerecognition.ui.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;
import com.ayuan.facerecognition.ui.peoples.PeopleActivity;
import com.ayuan.facerecognition.utils.CameraUtil;

import java.util.List;


/**
 * @author ayuan
 */
public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    private CardView cardAdd;
    private ListView lvList;
    private MainPresenter mainPresenter;
    private MainPeopleListAdapter mainPeopleListAdapter;
    private SwipeRefreshLayout refreshLayout;
    private CardView cardVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        cardAdd = findViewById(R.id.card_add);
        lvList = findViewById(R.id.lv_list);
        refreshLayout = findViewById(R.id.refreshLayout);
        cardVerification = findViewById(R.id.cardVerification);
    }

    private void initListener() {
        cardAdd.setOnClickListener((View v) -> {
            mainPresenter.addPeopleLibrary(this);
        });

        cardVerification.setOnClickListener((View v) -> {
            // 打开系统摄像头
            mainPresenter.requestPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        });

        refreshLayout.setOnRefreshListener(() -> {
            mainPresenter.updatePeopleLibrary();
        });
    }

    private void initData() {
        mainPresenter = new MainPresenter(this, new MainLogic());
        mainPresenter.initData();
    }

    /**
     * 设置数据适配器
     *
     * @param groupInfosBeans 数据适配器中需要填充的数据对象
     */
    @Override
    public void setPeopleList(List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfosBeans) {
        mainPeopleListAdapter = new MainPeopleListAdapter(this, groupInfosBeans);
        mainPeopleListAdapter.setOnItemClickListener(mainPresenter::onClick);
        lvList.setAdapter(mainPeopleListAdapter);
    }

    @Override
    public void refreshPeopleList() {
        mainPeopleListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    /**
     * 界面跳转
     *
     * @param groupId 人员库ID
     */
    @Override
    public void startActivity(String groupId) {
        Intent intent = new Intent(this, PeopleActivity.class);
        intent.putExtra("groupId", groupId);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void startCamera() {
        CameraUtil.startCamera(this);
    }

    @Override
    public void setImageData(Bitmap bmp) {
        // TODO: 2020/5/30
    }

    /**
     * @return 返回上下文环境
     */
    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mainPresenter.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}