package com.steveq.qroclock.ui.activities;


import com.steveq.qroclock.repo.Alarm;

import java.util.List;

public interface DataCollector {
    void shell();
    void withTime(String time);
    void withRepetition(List<String> days);
    void withRingtone(String ringtone);
    Alarm build();
}
