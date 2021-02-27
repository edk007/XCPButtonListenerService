package com.edtest.xcpbuttonlistenerservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

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

    static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, String status) {
        Intent intent = new Intent(context, com.edtest.xcpbuttonlistenerservice.XCPButtonActionService.class);
        intent.putExtra("STATUS",status);
        enqueueWork(context, com.edtest.xcpbuttonlistenerservice.XCPButtonActionService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        //write the action performed to a file
        String ts = DateFormat.getDateTimeInstance().format(new Date());
        String status = intent.getStringExtra("STATUS");
        String logStatus = status + " " + ts;
        Log.w(TAG, TAG2 + logStatus);
        writeToFile("TIME_STAMP: " + ts + ", " + System.currentTimeMillis() + " " + status + "\n");

        //broadcast status
        Intent statusIntent = new Intent(XCP_BAS_BROADCAST_ACTION)
                // Puts the status into the Intent
                .putExtra(XCP_BAS_SERVICE_STATUS, logStatus);
        // Broadcasts the Intent to receivers in this app.
        sendBroadcast(statusIntent);

        //TODO determine if it's a long press or a short press

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            FileOutputStream stream = new FileOutputStream(file, true);
            try {
                stream.write(data.getBytes());
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
