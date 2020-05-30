package com.ayuan.facerecognition.ui.peoples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;
import com.ayuan.facerecognition.ui.addPeople.AddPeopleActivity;

import java.util.List;

public class PeopleActivity extends AppCompatActivity implements PeopleView {
    public static final int CAMERA_RESULT_CODE = 200;
    private CardView cardAdd;
    private SwipeRefreshLayout refreshLayout;
    private ListView lvList;
    private CardView cardRemove;
    private PeoplePresenter peoplePresenter;
    private String groupId;
    private PeopleListAdapter peopleListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peoples);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        cardAdd = findViewById(R.id.cardAdd);
        refreshLayout = findViewById(R.id.refreshLayout);
        lvList = findViewById(R.id.lv_list);
        cardRemove = findViewById(R.id.cardRemove);
    }

    private void initEvent() {
        cardAdd.setOnClickListener((View v) -> {
            // 跳转到添加用户的界面
            Intent intent = new Intent(this, AddPeopleActivity.class);
            intent.putExtra("groupId", groupId);
            startActivityForResult(intent, CAMERA_RESULT_CODE);
        });

        cardRemove.setOnClickListener((View v) -> peoplePresenter.deleteGroup());

        refreshLayout.setOnRefreshListener(() -> peoplePresenter.updatePeopleList());

    }

    private void initData() {
        groupId = getIntent().getStringExtra("groupId");

        peoplePresenter = new PeoplePresenter(this, new PeopleLogic(), groupId);

        peoplePresenter.initData();
    }

    /**
     * 设置数据适配器
     *
     * @param personInfosBeans 需要设置的数据集合
     */
    @Override
    public void setPeopleAdapter(List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans) {
        peopleListAdapter = new PeopleListAdapter(this, personInfosBeans);
        lvList.setAdapter(peopleListAdapter);
    }

    @Override
    public void refreshPeopleList() {
        if (peopleListAdapter != null) {
            peopleListAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void closeActivity() {
        getIntent().putExtra("control", 1);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAMERA_RESULT_CODE == requestCode) {
            peoplePresenter.updatePeopleList();
        }
    }
}
