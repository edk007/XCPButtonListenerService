package com.edtest.xcpbuttonlistenerservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "XCP_BUTTON_LISTENER_SERVICE";
    public static final String TAG2 = "MAIN_ACTIVITY: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO - test if this even needs to ever be run - or if KSP can just call it without it ever having run - in which case nothign has to happen here - and no main activity needs to even exist?

        Handler splashHandler = new Handler(Looper.getMainLooper());
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                finish();
            }
        }, 2000);

    }
}