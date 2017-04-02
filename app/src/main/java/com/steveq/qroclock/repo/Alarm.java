package com.steveq.qroclock.repo;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "alarms")
public class Alarm implements Serializable{
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String time;

    @DatabaseField(defaultValue = "True")
    private Boolean active;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Day> days;

    @DatabaseField
    private String ringtoneUri;

    private List<Day> tempDays = new ArrayList<>();

    public Alarm() {
        setRingtoneUri("content://media/internal/audio/media/10");
    }

    public List<Day> getTempDays() {
        return tempDays;
    }

    public void setTempDays(List<Day> tempDays) {
        this.tempDays = tempDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ForeignCollection<Day> getDays() {
        return days;
    }

    public void setDays(ForeignCollection<Day> days) {
        this.days = days;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }
}
