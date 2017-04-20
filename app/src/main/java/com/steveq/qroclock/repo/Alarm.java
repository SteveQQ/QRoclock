package com.steveq.qroclock.repo;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "alarms")
public class Alarm{
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

    //List to keep selected days before saving them in ForeignCollection by clicking positive dialog button
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

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", active=" + active +
                ", days=" + days +
                ", ringtoneUri='" + ringtoneUri + '\'' +
                ", tempDays=" + tempDays +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        if (id != null ? !id.equals(alarm.id) : alarm.id != null) return false;
        if (time != null ? !time.equals(alarm.time) : alarm.time != null) return false;
        if (active != null ? !active.equals(alarm.active) : alarm.active != null) return false;
        if (days != null ? !days.equals(alarm.days) : alarm.days != null) return false;
        if (ringtoneUri != null ? !ringtoneUri.equals(alarm.ringtoneUri) : alarm.ringtoneUri != null)
            return false;
        return tempDays != null ? tempDays.equals(alarm.tempDays) : alarm.tempDays == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (days != null ? days.hashCode() : 0);
        result = 31 * result + (ringtoneUri != null ? ringtoneUri.hashCode() : 0);
        result = 31 * result + (tempDays != null ? tempDays.hashCode() : 0);
        return result;
    }
}
