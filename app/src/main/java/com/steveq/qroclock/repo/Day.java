package com.steveq.qroclock.repo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="days")
public class Day {
    public static final String ALARM_ID_FIELD_NAME = "alarm_id";

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String dayName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ALARM_ID_FIELD_NAME)
    private Alarm alarm;

    public Day() {
    }

    public Day(String name){
        this.dayName = name;
    }

    public int getId() {
        return id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}
