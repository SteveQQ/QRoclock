package com.steveq.qroclock.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Adam on 2017-04-10.
 */

public class AlarmHandlingService extends Service{
    private static final String TAG = AlarmHandlingService.class.getSimpleName();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BroadcastReceiver mReceiver = null;
    private static int numTicks = 0;

    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(Intent.ACTION_TIME_TICK)){
                    numTicks ++;
                    Log.d(TAG, "time tick action, tick : " + numTicks);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);

        HandlerThread thread = new HandlerThread("Time Tracking Thread", Thread.NORM_PRIORITY);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Handler run");
                if(numTicks > 1){
                    Log.d(TAG, "Num Ticks over 3");
                    unregisterReceiver(mReceiver);
                    stopSelf();
                }
                mServiceHandler.postDelayed(this, 30000);
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
