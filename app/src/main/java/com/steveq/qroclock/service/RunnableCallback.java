package com.steveq.qroclock.service;

/**
 * Created by Adam on 2017-04-11.
 */

public interface RunnableCallback {
    void timeHasCome(Runnable r, AlarmHandlingService.AlarmInfo info);
}
