package com.ayuan.facerecognition.ui.main;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ayuan.facerecognition.R;
import com.google.android.material.card.MaterialCardView;

public class DialogViewHolder {
    private final AlertDialog alertDialog;
    public View rootView;
    public ImageView ivAvatar;
    public TextView tvName;
    public MaterialCardView cardConfirm;

    public DialogViewHolder(View rootView, AlertDialog alertDialog) {
        this.rootView = rootView;
        this.ivAvatar = rootView.findViewById(R.id.ivAvatar);
        this.tvName = rootView.findViewById(R.id.tvName);
        this.cardConfirm = rootView.findViewById(R.id.cardConfirm);
        this.alertDialog = alertDialog;
    }

    public void init() {
        cardConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void setName(String name) {
        this.tvName.setText(name);
    }

    public void setImage(Bitmap bitmap) {
        this.ivAvatar.setImageBitmap(bitmap);
    }
}
