package com.steveq.qroclock.ui.activities;


import com.j256.ormlite.dao.ForeignCollection;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.repo.Days;

import java.util.List;

public interface DataCollector {
    void init();
    void init(Alarm alarm);
    void withTime(String time);
    void withRepetition(ForeignCollection<Day> days);
    void withRingtone(String ringtone);
    Alarm getInstance();
}
