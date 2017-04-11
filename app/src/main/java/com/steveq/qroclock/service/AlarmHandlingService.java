package com.steveq.qroclock.service;

import android.app.Notification;
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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.steveq.qroclock.ui.activities.AlarmRingActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adam on 2017-04-10.
 */

public class AlarmHandlingService extends Service implements RunnableCallback{
    private static final String TAG = AlarmHandlingService.class.getSimpleName();
    public static String ALARM_TIME = "ALARM TIME";
    public static String ALARM_RINGTONE = "ALARM RINGTONE";
    public static String ALARM_INFO = "ALARM INFO";
    public static int FOREGROUND_ID = 10;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BroadcastReceiver mReceiver = null;
    private static int numTicks = 0;
    private Set<Runnable> mTasksToExecute;

    public class AlarmInfo {
        private String time;
        private String ringtone;

        public AlarmInfo(String time, String ringtone) {
            this.time = time;
            this.ringtone = ringtone;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRingtone() {
            return ringtone;
        }

        public void setRingtone(String ringtone) {
            this.ringtone = ringtone;
        }
    }

    @Override
    public void timeHasCome(Runnable r, AlarmInfo info) {
        mTasksToExecute.remove(r);
        Intent intent = new Intent(this, AlarmRingActivity.class);
        intent.putExtra(ALARM_RINGTONE, info.getRingtone());
        startActivity(intent);
        if(mTasksToExecute.size() == 0){
            Log.d(TAG, "Stopping the service...");
            stopSelf();
        }
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
                    Log.d(TAG, "Runnables to run : " + mTasksToExecute.size());

                    for(Runnable r : mTasksToExecute){
                        mServiceHandler.post(r);
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);

        HandlerThread thread = new HandlerThread("Time Tracking Thread", Thread.NORM_PRIORITY);
        thread.start();

        startForeground(FOREGROUND_ID, buildForegroundNotification());

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mTasksToExecute = new HashSet<>();
    }

    private Notification buildForegroundNotification() {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle("Alarm")
                .setContentText("Alarm")
                .setSmallIcon(android.R.drawable.stat_sys_download);

        return(b.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        AlarmInfo aInfo = new AlarmInfo(intent.getStringExtra(ALARM_TIME), intent.getStringExtra(ALARM_RINGTONE));
        mTasksToExecute.add(new AlarmRunnable(aInfo, AlarmHandlingService.this));

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
