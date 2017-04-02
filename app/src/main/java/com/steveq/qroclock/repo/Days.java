package com.steveq.qroclock.repo;

import com.j256.ormlite.table.DatabaseTable;

public enum Days {
    MONDAY(1, "mon."),
    TUESDAY(2, "tue."),
    WEDNESDAY(3, "wed."),
    THURSDAY(4, "thu."),
    FRIDAY(5, "fri."),
    SATURDAY(6, "sat."),
    SUNDAY(7, "sun.");

    Integer dayNum;
    String abb;

    Days(Integer dayNum, String abb){
        this.dayNum = dayNum;
        this.abb = abb;
    }

    public String getAbb() {
        return abb;
    }
}
