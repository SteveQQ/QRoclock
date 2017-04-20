package com.steveq.qroclock.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.steveq.qroclock.R;
import com.steveq.qroclock.database.AlarmsManager;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.ui.activities.QRScannerActivity;

import java.util.HashSet;
import java.util.List;
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
    private BroadcastReceiver mTimeTickReceiver = null;
    private BroadcastReceiver mAlarmStopReceiver = null;
    private Ringtone mRingtone;
    private Boolean isWaking;

    private static int numTicks = 0;
    private Set<AlarmRunnable> mTasksToExecute;

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
    public void timeHasCome(Runnable r, Alarm alarm) {
        Intent intent = new Intent(this, QRScannerActivity.class);
        intent.putExtra(QRScannerActivity.GET_STATE, QRScannerActivity.STOP_WAKING);
        startActivity(intent);
        Uri ringUri = Uri.parse(alarm.getRingtoneUri());
        mRingtone = RingtoneManager.getRingtone(this, ringUri);
        isWaking = true;

        updateNotification(alarm.getTime());
        wakeUp(alarm);
    }

    private void updateNotification(String time) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, QRScannerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification not = new Notification.Builder(this)
                .setContentTitle("QR O'clock")
                .setContentText("You have active alarms.")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_alarm_vec)
                .build();
        manager.notify(FOREGROUND_ID, not);
    }

    private void wakeUp(final Alarm alarm){
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //Playing ringtone in separate thread because it blocked UI thread
        HandlerThread thread = new HandlerThread("Ringtone play thread");
        thread.start();
        Handler handler = new Handler(thread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                while(isWaking){
                    if(!mRingtone.isPlaying()) {
                        mRingtone.play();
                        //volume ringtone up
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                    }
                }
                alarm.setActive(false);
                AlarmsManager.getInstance(AlarmHandlingService.this).updateAlarmActiveStatus(alarm);
                updateTasks();
            }
        });
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

        mTimeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_TIME_TICK)){
                Log.d(TAG, "Runnables to run : " + mTasksToExecute.size());
                for(Runnable r : mTasksToExecute){
                    mServiceHandler.post(r);
                }
                updateTasks();
            }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeTickReceiver, filter);

        mAlarmStopReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isWaking = false;

            }
        };
        filter = new IntentFilter();
        filter.addAction("com.steveq.qroclock.ALARM_STOP");
        registerReceiver(mAlarmStopReceiver, filter);

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
                .setContentTitle("QR O'clock")
                .setContentText("You have active alarms.")
                .setSmallIcon(R.drawable.ic_alarm_vec);

        return(b.build());
    }

    private void updateTasks(){
        List<Alarm> activeAlarms = AlarmsManager.getInstance(this).readActiveAlarms();
        Log.d(TAG, activeAlarms.toString());
        if(activeAlarms.size() > 0){
            if(mTasksToExecute.size() > 0){
                for(Alarm alarm : activeAlarms){
                    for(AlarmRunnable runnable : mTasksToExecute){
                        if(alarm.equals(runnable.getmAlarm())){
                            runnable.setmAlarm(alarm);
                        }
                    }
                }
            } else {
                for(Alarm alarm :activeAlarms){
                    mTasksToExecute.add(new AlarmRunnable(alarm, AlarmHandlingService.this));
                }
            }
        } else {
            mTasksToExecute = null;
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("com.steveq.qroclock.UPDATE_ALARM".equals(intent.getAction())){
            Log.d(TAG, "Updating alarms tasks...");
            updateTasks();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeTickReceiver);
        unregisterReceiver(mAlarmStopReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
