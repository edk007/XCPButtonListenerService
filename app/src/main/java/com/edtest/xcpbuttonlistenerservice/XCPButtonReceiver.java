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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class XCPButtonReceiver extends BroadcastReceiver {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "XCP_BUTTON_RECEIVER: ";

    public static final String PTT_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_PRESS";
    public static final String PTT_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_RELEASE";
    public static final String TOP_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_PRESS";
    public static final String TOP_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_RELEASE";

    public static final String XCP_BAS_SERVICE_STATUS = "com.edtest.xcpbuttonlistenerservice.STATUS";
    public static final String XCP_BAS_BROADCAST_ACTION = "com.edtest.xcpbuttonlistenerservice.BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, TAG2 + "ON_RECEIVE");

        //not using this
        //XCPButtonActionService.enqueueWork(context,"PTT_KEY_PRESSED");

        //TODO - using a runnable delayed does not work for long press
        //TODO - need to think about alternate structure for long press actions?

        String btnString = "NONE";
        boolean torchState = false;
        switch (intent.getAction()) {
            case PTT_PRESS:
                btnString = "PTT_KEY_PRESSED";
                torchState = true;
                break;
            case PTT_RELEASE:
                btnString = "PTT_KEY_RELEASED";
                torchState = false;
                break;
            case TOP_PRESS:
                btnString = "TOP_KEY_PRESSED";
                torchState = true;
                break;
            case TOP_RELEASE:
                btnString = "TOP_KEY_RELEASED";
                torchState = false;
                break;
            default:
        }
        //log this in logcat
        Log.w(TAG, TAG2 + btnString);
        //write to the file
        writeToFile(context, btnString);
        //broadcast to other apps
        sendAppBroadcast(context, btnString);
        //turn on the torch
        String mCameraId = null;
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

    private void writeToFile(Context context, String data) {
        String TAG3 = "WRITE_FILE_OUTPUT: ";
        String fileName = "XCP_BUTTON_LISTENER_SERVICE.txt";
        File file;
        File saveFilePath;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            saveFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else  {
            saveFilePath = context.getExternalFilesDir(null);
        }
        Log.w(TAG, TAG2 + TAG3 + "LOG_FILE_PATH: " + saveFilePath.toString());
        file = new File(saveFilePath, fileName);

        try {
            FileOutputStream stream = new FileOutputStream(file, true);
            try {
                stream.write(data.getBytes());
            } finally {
                Log.w(TAG, TAG2 + TAG3 + "WRITE_SUCCESS");
                stream.close();
            }
        } catch (IOException e) {
            Log.w(TAG, TAG2 + TAG3 + "WRITE_FAIL");
            e.printStackTrace();
        }
    }//WriteToFile

    private void sendAppBroadcast(Context context, String string) {
        Log.w(TAG,TAG2 + "SENDING_APP_BROADCAST");
        Intent statusIntent = new Intent(XCP_BAS_BROADCAST_ACTION);
        statusIntent.putExtra(XCP_BAS_SERVICE_STATUS, string);
        context.sendBroadcast(statusIntent);
    }//sendAppBroadcast
}