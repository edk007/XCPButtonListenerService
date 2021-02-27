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

    public static final String PTT_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_PRESS";
    public static final String PTT_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.PTT_RELEASE";
    public static final String TOP_PRESS = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_PRESS";
    public static final String TOP_RELEASE = "com.edtest.xcpbuttonlistenerservice.intent.action.TOP_RELEASE";

    public static final String XCP_BAS_SERVICE_STATUS = "com.edtest.xcpbuttonlistenerservice.STATUS";
    public static final String XCP_BAS_BROADCAST_ACTION = "com.edtest.xcpbuttonlistenerservice.BROADCAST";

    public static final String LOCAL_ACTION = "LOCAL_ACTION";
    public static final String LOCAL_MESSAGE = "LOCAL_MESSAGE";

    ArrayList<String> buttonActions;
    ListView listView;
    ArrayAdapter arrayAdapter;

    XCPButtonReceiver xcpButtonReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO eventually remove all UI and this is just a service
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        buttonActions = new ArrayList<>();
        buttonActions.add("STARTING");

        arrayAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.label, buttonActions);

        listView.setAdapter(arrayAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiveLocalBroadcast, new IntentFilter(LOCAL_ACTION));
        registerReceiver(receiveAppBroadcast, new IntentFilter(XCP_BAS_BROADCAST_ACTION));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.w(TAG, TAG2 + "ON_POST_RESUME");
        xcpButtonReceiver = new XCPButtonReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PTT_PRESS);
        intentFilter.addAction(PTT_RELEASE);
        intentFilter.addAction(TOP_PRESS);
        intentFilter.addAction(TOP_RELEASE);
        registerReceiver(xcpButtonReceiver,intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(xcpButtonReceiver);
    }

    private BroadcastReceiver receiveLocalBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, TAG2 + "RECEIVE_LOCAL_BROADCAST_ON_RECEIVE");
            if (LOCAL_ACTION.equals(intent.getAction())) {
                Log.w(TAG, TAG2 + "RECEIVE_LOCAL_BROADCAST_INTENT_ACTION_MATCH");
                String message = intent.getStringExtra(LOCAL_MESSAGE);
                buttonActions.add(0, message);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private BroadcastReceiver receiveAppBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, TAG2 + "RECEIVE_APP_BROADCAST_ON_RECEIVE");
            if (XCP_BAS_BROADCAST_ACTION.equals(intent.getAction())) {
                Log.w(TAG, TAG2 + "RECEIVE_APP_BROADCAST_INTENT_ACTION_MATCH");
                String message = intent.getStringExtra(XCP_BAS_SERVICE_STATUS);
                buttonActions.add(0, message);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

}