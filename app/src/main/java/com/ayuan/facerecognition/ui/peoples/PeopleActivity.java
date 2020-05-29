package com.ayuan.facerecognition.ui.peoples;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;

import java.util.List;

public class PeopleActivity extends AppCompatActivity implements PeopleView {
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

        });

        cardRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        refreshLayout.setOnRefreshListener(() -> peoplePresenter.updatePeopleList(groupId));

    }

    private void initData() {
        groupId = getIntent().getStringExtra("groupId");

        peoplePresenter = new PeoplePresenter(this, new PeopleLogic());

        peoplePresenter.initData();

        peoplePresenter.getPersonList(groupId);
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
        peopleListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
}
