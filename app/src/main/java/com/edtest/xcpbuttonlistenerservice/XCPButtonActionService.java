package com.edtest.xcpbuttonlistenerservice;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class XCPButtonActionService extends JobIntentService {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "XCP_BUTTON_ACTION_SERVICE: ";

    public static final String XCP_BAS_SERVICE_STATUS = "com.edtest.xcpbuttonlistenerservice.STATUS";
    public static final String XCP_BAS_BROADCAST_ACTION = "com.edtest.xcpbuttonlistenerservice.BROADCAST";

    public static final String LOCAL_ACTION = "LOCAL_ACTION";
    public static final String LOCAL_MESSAGE = "LOCAL_MESSAGE";

    static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, String status) {
        Log.w(TAG, TAG2 + "ENQUEUE_WORK");
        Intent intent = new Intent(context, com.edtest.xcpbuttonlistenerservice.XCPButtonActionService.class);
        intent.putExtra("STATUS",status);
        enqueueWork(context, com.edtest.xcpbuttonlistenerservice.XCPButtonActionService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.w(TAG, TAG2 + "HANDLE_WORK");

        //write the action performed to a file
        String ts = DateFormat.getDateTimeInstance().format(new Date());
        String status = intent.getStringExtra("STATUS");
        String msLogStatus = status + System.currentTimeMillis();
        Log.w(TAG, TAG2 + msLogStatus);
        writeToFile("TIME_STAMP: " + ts + ", " + System.currentTimeMillis() + " " + status + "\n");
        sendAppBroadcast(this, msLogStatus);
        sendLocalBroadcast(this, msLogStatus);

        //turn the light on and off for the PTT key press and release as a test
        String mCameraId = null;
        CameraManager mCameraManager;
        mCameraManager = (CameraManager) XCPButtonActionService.this.getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (status.equals("XCP_PTT_KEY_PRESSED")) {
            Log.w(TAG, TAG2 + "TORCH_ON");
            try {
                mCameraManager.setTorchMode(mCameraId,true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else if (status.equals("XCP_PTT_KEY_RELEASED")) {
            Log.w(TAG, TAG2 + "TORCH_OFF");
            try {
                mCameraManager.setTorchMode(mCameraId,false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        //TODO determine if it's a long press or a short press

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, TAG2 + "ON_DESTROY");
    }

    private void writeToFile(String data) {
        String TAG3 = "WRITE_FILE_OUTPUT: ";
        Log.w(TAG,TAG2 + TAG3);
        String fileName = "XCP_BUTTON_LISTENER_SERVICE.txt";
        File externalFilesPath = com.edtest.xcpbuttonlistenerservice.XCPButtonActionService.this.getExternalFilesDir(null);
        Log.w(TAG, TAG2 + TAG3 + "FILE_NAME: " + fileName);
        Log.w(TAG, TAG2 + TAG3 + "EXTERNAL_FILE_PATH: " + externalFilesPath.toString());

        File file = new File(externalFilesPath, fileName);
        try {
            Log.w(TAG,TAG2 + "WRITING_FILE_OUTPUT");
            FileOutputStream stream = new FileOutputStream(file, true);
            try {
                stream.write(data.getBytes());
                Log.w(TAG,TAG2 + "WRITING_FILE_OUTPUT_SUCCESS");
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            Log.w(TAG,TAG2 + "WRITING_FILE_OUTPUT_FAIL");
            e.printStackTrace();
        }
    }

    private void sendLocalBroadcast(Context context, String string) {
        Log.w(TAG,TAG2 + "SENDING_LOCAL_BROADCAST");
        Intent intent = new Intent(LOCAL_ACTION);
        intent.putExtra(LOCAL_MESSAGE, string);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendAppBroadcast(Context context, String string) {
        Log.w(TAG,TAG2 + "SENDING_APP_BROADCAST");
        Intent statusIntent = new Intent(XCP_BAS_BROADCAST_ACTION);
        statusIntent.putExtra(XCP_BAS_SERVICE_STATUS, string);
        sendBroadcast(statusIntent);
    }

}
