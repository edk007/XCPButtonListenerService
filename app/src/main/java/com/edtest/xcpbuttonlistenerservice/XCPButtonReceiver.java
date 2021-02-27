package com.edtest.xcpbuttonlistenerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XCPButtonReceiver extends BroadcastReceiver {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "XCP_BUTTON_RECEIVER: ";

    public static final String PTT_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_PRESS";
    public static final String PTT_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_RELEASE";
    public static final String TOP_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_PRESS";
    public static final String TOP_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_RELEASE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, TAG2 + "ON_RECEIVE");

        String status = "BLANK";

        if (PTT_PRESS.equals(intent.getAction())) {
            // XCover key pressed
            Log.w(TAG, TAG2 + "XCP_PTT_KEY_PRESSED");
            XCPButtonActionService.enqueueWork(context,"XCP_PTT_KEY_PRESSED");
            status = "XCP_KEY_PRESSED";
        }
        else if (PTT_RELEASE.equals(intent.getAction())) {
            //XCover Key Released
            Log.w(TAG, TAG2 + "XCP_PTT_KEY_RELEASED");
            XCPButtonActionService.enqueueWork(context,"XCP_PTT_KEY_RELEASED");
            status = "XCP_PTT_KEY_RELEASED";
        }
        else if (TOP_PRESS.equals(intent.getAction())) {
            // XCover key pressed
            Log.w(TAG, TAG2 + "XCP_TOP_KEY_PRESSED");
            XCPButtonActionService.enqueueWork(context,"XCP_TOP_KEY_PRESSED");
            status = "XCP_TOP_KEY_PRESSED";
        }
        else if (TOP_RELEASE.equals(intent.getAction())) {
            //XCover Key Released
            Log.w(TAG, TAG2 + "XCP_TOP_KEY_RELEASED");
            XCPButtonActionService.enqueueWork(context,"XCP_TOP_KEY_RELEASED");
            status = "XCP_TOP_KEY_RELEASED";
        }
    }
}