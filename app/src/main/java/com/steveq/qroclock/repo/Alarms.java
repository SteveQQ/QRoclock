package com.steveq.qroclock.repo;


import java.io.Serializable;
import java.util.List;

public class Alarms implements Serializable{
    private List<Alarm> alarms;
    private String message;

    public Alarms(){}

    public Alarms(List<Alarm> alarms, String message) {
        this.alarms = alarms;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        builder.append("message = ");
        builder.append(message);
        builder.append("}");
        return builder.toString();
    }
}
