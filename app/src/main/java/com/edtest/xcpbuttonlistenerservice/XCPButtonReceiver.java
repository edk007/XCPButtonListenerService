package com.edtest.xcpbuttonlistenerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class XCPButtonReceiver extends BroadcastReceiver {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "XCP_BUTTON_RECEIVER: ";

    public static final String PTT_PRESS_INTENT = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_PRESS";
    public static final String PTT_RELEASE_INTENT = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_RELEASE";
    public static final String TOP_PRESS_INTENT = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_PRESS";
    public static final String TOP_RELEASE_INTENT = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_RELEASE";

    public static final String XCP_BAS_BUTTON_STRING = "com.edtest.xcpbuttonlistenerservice.BUTTON_STRING";
    public static final String XCP_BAS_BUTTON_INT = "com.edtest.xcpbuttonlistenerservice.BUTTON_INT";
    public static final String XCP_BAS_BROADCAST_ACTION = "com.edtest.xcpbuttonlistenerservice.BROADCAST";

    public static final int PTT_PRESS = 0;
    public static final int PTT_RELEASE = 1;
    public static final int TOP_PRESS = 2;
    public static final int TOP_RELEASE = 3;
    public static final int BTN_ERROR = 99;

    //variables to setup timing for the long press
    private Handler longPressHandler_PTT, longPressHandler_TOP;
    int long_press_time = 2000; //milliseconds

    int started=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, TAG2 + "ON_RECEIVE");
        if (started==0) {
            started = new Random().nextInt(100)+1;
        }

        longPressHandler_PTT = new Handler(Looper.getMainLooper());
        longPressHandler_TOP = new Handler(Looper.getMainLooper());

        String btnString = "NONE";
        int btnInt = BTN_ERROR;
        boolean torchState = false;
        switch (intent.getAction()) {
            case PTT_PRESS_INTENT:
                btnString = "PTT_KEY_PRESSED";
                btnInt = PTT_PRESS;
                torchState = true;
                break;
            case PTT_RELEASE_INTENT:
                btnString = "PTT_KEY_RELEASED";
                btnInt = PTT_RELEASE;
                torchState = false;
                break;
            case TOP_PRESS_INTENT:
                btnString = "TOP_KEY_PRESSED";
                btnInt = TOP_PRESS;
                torchState = true;
                break;
            case TOP_RELEASE_INTENT:
                btnString = "TOP_KEY_RELEASED";
                btnInt = TOP_RELEASE;
                torchState = false;
                break;
            default:
        }

        btnString = btnString + "_RND:" + started;

        //log this in logcat
        Log.w(TAG, TAG2 + btnString);

        //broadcast to other apps and services
        sendAppBroadcast(context, btnString, btnInt);

        //write to file
        writeToFile(context,btnString);

        //turn on/off the torch as a visual test it's working
        String mCameraId;
        CameraManager mCameraManager;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(mCameraId,torchState);
            Log.w(TAG, TAG2 + "TORCH");
        } catch (CameraAccessException e) {
            Log.w(TAG, TAG2 + "TORCH_FAIL");
            e.printStackTrace();
        }

    }//onReceive

    private void sendAppBroadcast(Context context, String string, int i) {
        Log.w(TAG,TAG2 + "SENDING_APP_BROADCAST");
        Intent statusIntent = new Intent(XCP_BAS_BROADCAST_ACTION);
        statusIntent.putExtra(XCP_BAS_BUTTON_STRING, string);
        statusIntent.putExtra(XCP_BAS_BUTTON_INT, i);
        context.sendBroadcast(statusIntent);
    }//sendAppBroadcast

    private final Runnable longPressRunnable_PTT = () -> {
        //we have a long press on the PTT button
        Log.w(TAG, TAG2 + "PTT_LONG_PRESS");
    };

    private final Runnable longPressRunnable_TOP = () -> {
        //we have a long press on the top button
        Log.w(TAG, TAG2 + "TOP_LONG_PRESS");
    };

    private void writeToFile(Context context, String data) {
        String TAG3 = "WRITE_FILE_OUTPUT: ";
        String fileName = "XCP_BUTTON_LISTENER_SERVICE.txt";
        File file;
        File saveFilePath;
        data = System.currentTimeMillis() + ", " + data;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            saveFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else  {
            saveFilePath = context.getExternalFilesDir(null);
        }
        Log.w(TAG, TAG2 + TAG3 + "LOG_FILE_PATH: " + saveFilePath.toString());
        file = new File(saveFilePath, fileName);

        try {
            try (FileOutputStream stream = new FileOutputStream(file, true)) {
                stream.write(data.getBytes());
            } finally {
                Log.w(TAG, TAG2 + TAG3 + "WRITE_SUCCESS");
            }
        } catch (IOException e) {
            Log.w(TAG, TAG2 + TAG3 + "WRITE_FAIL");
            e.printStackTrace();
        }
    }//WriteToFile

}