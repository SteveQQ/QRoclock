package com.steveq.qroclock.service;


import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class TimeTrackerService extends IntentService {
    public static final String TAG = TimeTrackerService.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "message";
    private BroadcastReceiver mReceiver = null;

    public TimeTrackerService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(Intent.ACTION_TIME_TICK)){
                    Log.d(TAG, "time tick action");
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        Log.d(TAG, "KOMUNIKAT : " + text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
