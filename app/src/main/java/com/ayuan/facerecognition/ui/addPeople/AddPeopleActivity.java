package com.ayuan.facerecognition.ui.addPeople;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ayuan.facerecognition.R;
import com.ayuan.facerecognition.utils.CameraUtil;

public class AddPeopleActivity extends AppCompatActivity implements AddPeopleView {
    private static final String TAG = "AddPeopleActivity";
    public static final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView ivIcon;
    private CardView cardAddPicture;
    private CardView cardSubmit;
    private EditText etName;
    private EditText etId;
    private RadioGroup radioSelectGender;
    private RadioButton radioMan;
    private RadioButton radioWoman;
    private AddPeoplePresenter addPeoplePresenter;
    private int isMan = 1;
    private String groupId;
    private Bitmap faceBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeople);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        ivIcon = findViewById(R.id.ivIcon);
        cardAddPicture = findViewById(R.id.cardAddPicture);
        cardSubmit = findViewById(R.id.cardSubmit);
        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        radioSelectGender = findViewById(R.id.radioSelectGender);
        radioMan = findViewById(R.id.radioMan);
        radioWoman = findViewById(R.id.radioWoman);
    }

    private void initEvent() {
        cardAddPicture.setOnClickListener(v -> addPeoplePresenter.startCameraAndRequestPermission(AddPeopleActivity.this, PERMISSIONS));

        cardSubmit.setOnClickListener(v -> {
            addPeoplePresenter.submit(faceBitmap, etName.getText().toString().trim(), etId.getText().toString().trim(), isMan);
        });

        radioSelectGender.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            if (checkedId == R.id.radioMan) {
                isMan = 1;
            } else if (checkedId == R.id.radioWoman) {
                isMan = 2;
            }
        });
    }

    private void initData() {
        groupId = getIntent().getStringExtra("groupId");

        addPeoplePresenter = new AddPeoplePresenter(this, new AddPeopleLogic(), groupId);
        addPeoplePresenter.initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        addPeoplePresenter.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void startCamera() {
        CameraUtil.startCamera(this);
    }

    @Override
    public void getCameraBitmap(Bitmap bmp) {
        this.faceBitmap = bmp;
        ivIcon.setImageBitmap(bmp);
    }

    /**
     * 关闭当前界面
     */
    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addPeoplePresenter.onActivityResult(requestCode, resultCode, data);
    }
}
