package com.ayuan.facerecognition;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

public class App extends Application {

    private static App context;
    private static Toast toast;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    public static void showToast(String message) {
        toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
