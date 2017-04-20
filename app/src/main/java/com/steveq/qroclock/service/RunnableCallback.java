package com.steveq.qroclock.service;

import com.steveq.qroclock.repo.Alarm;

/**
 * Created by Adam on 2017-04-11.
 */

public interface RunnableCallback {
    void timeHasCome(Runnable r, Alarm alarm);
}
