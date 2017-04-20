package com.steveq.qroclock.service;

import android.app.Service;
import android.os.Handler;
import android.util.Log;

import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.repo.Days;

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
    private Alarm mAlarm;
    private static int tick;

    public AlarmRunnable(Alarm alarm, RunnableCallback callback){
        mAlarm = alarm;
        mCallback = callback;
    }

    public void setmAlarm(Alarm mAlarm) {
        this.mAlarm = mAlarm;
    }

    public Alarm getmAlarm() {
        return mAlarm;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        String curTime = sdf.format(calendar.getTime());

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //If days to repeat are set
        if(mAlarm.getDays().size() > 0){
            //For every day set for alarm
            for(Day d : mAlarm.getDays()){
                //If day of week is equals current day of Week
                if(Days.valueOf(d.getDayName()).getDayNum() == dayOfWeek){
                    //If time matches
                    if(curTime.equals(mAlarm.getTime())){
                        //ring
                        mCallback.timeHasCome(this, mAlarm);
                    }
                }
            }
        } else {
            //If time matches
            if(curTime.equals(mAlarm.getTime())){
                //Automatically set this alarm to inactive
                mAlarm.setActive(false);
                //ring
                mCallback.timeHasCome(this, mAlarm);
            }
        }

    }
}
