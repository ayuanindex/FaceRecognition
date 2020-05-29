package com.ayuan.facerecognition.ui.peoples;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.tencentCloud.bean.PersonListBean;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class PeopleListAdapter extends BaseAdapter {

    private final List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans;
    private final PeopleActivity peopleActivity;
    private MaterialCardView cardItem;
    private ImageView ivImg;
    private MaterialTextView tvGroupName;
    private MaterialTextView tvGroupId;

    public PeopleListAdapter(PeopleActivity peopleActivity, List<PersonListBean.ResponseBean.PersonInfosBean> personInfosBeans) {
        this.peopleActivity = peopleActivity;
        this.personInfosBeans = personInfosBeans;
    }

    @Override
    public int getCount() {
        return personInfosBeans.size();
    }

    @Override
    public PersonListBean.ResponseBean.PersonInfosBean getItem(int position) {
        return personInfosBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(peopleActivity, R.layout.item_peoples, null);
        } else {
            view = convertView;
        }
        initView(view);
        tvGroupName.setText(getItem(position).getPersonName());
        tvGroupId.setText(getItem(position).getPersonId());
        return view;
    }

    private void initView(View view) {
        cardItem = view.findViewById(R.id.cardItem);
        ivImg = view.findViewById(R.id.iv_img);
        tvGroupName = view.findViewById(R.id.tvGroupName);
        tvGroupId = view.findViewById(R.id.tvGroupId);
    }
}
