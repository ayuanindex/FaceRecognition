package com.ayuan.facerecognition.ui.addPeople;

import android.graphics.Bitmap;

public interface AddPeopleView {
    /**
     * 打开相机
     */
    void startCamera();

    /**
     * 获取相机拍摄的照片
     *
     * @param bmp 获取到的照片
     */
    void getCameraBitmap(Bitmap bmp);
}
