package com.steveq.qroclock.repo;


import java.io.Serializable;
import java.util.List;

public class Alarm implements Serializable{
    private String time;
    private Boolean active;
    private List<Days> days;
    private String ringtoneUri;

    public Alarm() {
        this.setActive(true);
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
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

    public List<Days> getDays() {
        return days;
    }

    public void setDays(List<Days> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "time='" + time + '\'' +
                ", active=" + active +
                ", days=" + days +
                '}';
    }
}
