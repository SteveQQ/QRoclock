package com.steveq.qroclock.ui.activities;


import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Days;

import java.util.List;

public interface DataCollector {
    void init();
    void withTime(String time);
    void withRepetition(List<Days> days);
    void withRingtone(String ringtone);
    Alarm getInstance();
}
