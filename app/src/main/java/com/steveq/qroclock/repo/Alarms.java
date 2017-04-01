package com.steveq.qroclock.repo;


import java.io.Serializable;
import java.util.List;

public class Alarms implements Serializable{
    private List<Alarm> alarms;

    public Alarms(){}

    public Alarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Alarms{");
        for(Alarm a : alarms){
            builder.append(a.toString());
            builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
