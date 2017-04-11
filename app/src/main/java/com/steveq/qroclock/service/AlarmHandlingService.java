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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 2017-04-10.
 */

public class AlarmHandlingService extends Service implements RunnableCallback{
    private static final String TAG = AlarmHandlingService.class.getSimpleName();
    public static String ALARM_TIME = "ALARM TIME";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BroadcastReceiver mReceiver = null;
    private static int numTicks = 0;
    private Set<Runnable> mTasksToExecute;

    @Override
    public void timeHasCome(Runnable r) {
        mTasksToExecute.remove(r);
    }

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
                    Log.d(TAG, "Runnables to run : " + mTasksToExecute.size());
                    for(Runnable r : mTasksToExecute){
                        mServiceHandler.post(r);
                    }
                    if(mTasksToExecute.size() == 0){
                        stopSelf();
                    }
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
        mTasksToExecute = new HashSet<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        mTasksToExecute.add(new AlarmRunnable(intent.getStringExtra(ALARM_TIME), AlarmHandlingService.this));
//        mServiceHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Handler run");
//                if(numTicks > 1){
//                    Log.d(TAG, "Num Ticks over 3");
//                    unregisterReceiver(mReceiver);
//                    stopSelf();
//                }
//                mServiceHandler.postDelayed(this, 30000);
//            }
//        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister Receiver");
        unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
