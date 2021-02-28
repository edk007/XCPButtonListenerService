package com.edtest.xcpbuttonlistenerservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "MAIN_ACTIVITY: ";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //it's not necessary to launch this app if we use the KSP button intents to trigger the BroadcastReceiver
        setContentView(R.layout.activity_main2);

        //use a handler and runnable to popup a loading screen - can be branded
        Handler splashHandler = new Handler(Looper.getMainLooper());
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}