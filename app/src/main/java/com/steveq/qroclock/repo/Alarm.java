package com.steveq.qroclock.repo;


import java.io.Serializable;
import java.util.List;

public class Alarm implements Serializable{
    private String time;
    private Boolean active;
    private List<String> days;

    public Alarm() {
    }

    public Alarm(String time, Boolean active, List<String> days) {
        this.time = time;
        this.active = active;
        this.days = days;
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

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
