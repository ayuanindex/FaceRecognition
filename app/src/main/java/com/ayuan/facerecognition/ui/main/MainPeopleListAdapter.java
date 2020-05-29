package com.ayuan.facerecognition.ui.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.tencentCloud.bean.GetPeopleLibraryBean;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MainPeopleListAdapter extends BaseAdapter {

    private final List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfos;
    private final Context context;
    private MaterialTextView tvGroupName;
    private MaterialTextView tvGroupId;
    private MaterialTextView tvTag;
    private MaterialCardView cardItem;
    private OnItemClickListener onItemClickListener;

    public MainPeopleListAdapter(Context context, List<GetPeopleLibraryBean.ResponseBean.GroupInfosBean> groupInfos) {
        this.context = context;
        this.groupInfos = groupInfos;
    }

    interface OnItemClickListener {
        void onClick(GetPeopleLibraryBean.ResponseBean.GroupInfosBean item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return groupInfos.size();
    }

    @Override
    public GetPeopleLibraryBean.ResponseBean.GroupInfosBean getItem(int position) {
        return groupInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_person_library, null);
        } else {
            view = convertView;
        }
        initView(view);
        tvGroupName.setText(getItem(position).getGroupName());
        tvGroupId.setText(getItem(position).getGroupId());
        tvTag.setText(TextUtils.isEmpty(getItem(position).getTag()) ? "暂无TAG" : getItem(position).getTag());

        cardItem.setOnClickListener(null);
        cardItem.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(getItem(position));
            }
        });
        return view;
    }

    private void initView(View view) {
        tvGroupName = view.findViewById(R.id.tvGroupName);
        tvGroupId = view.findViewById(R.id.tvGroupId);
        tvTag = view.findViewById(R.id.tvTag);
        cardItem = view.findViewById(R.id.cardItem);
    }
}
