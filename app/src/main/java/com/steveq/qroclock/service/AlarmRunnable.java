package com.steveq.qroclock.service;

import android.app.Service;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Adam on 2017-04-11.
 */

public class AlarmRunnable implements Runnable {
    private static final String TAG = AlarmRunnable.class.getSimpleName();
    private String mTimeForAlarm;
    private Service mParent;
    private RunnableCallback mCallback;
    private static int tick;

    public AlarmRunnable(String time, RunnableCallback callback){
        mTimeForAlarm = time;
        mCallback = callback;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        String curTime = sdf.format(calendar.getTime());
        System.out.println("Current time : " + curTime);
        System.out.println("Set time : " + mTimeForAlarm);
        if(curTime.equals(mTimeForAlarm)){
            Log.d(TAG, "remove this runnable instance from set");
            mCallback.timeHasCome(this);
        }
    }
}
