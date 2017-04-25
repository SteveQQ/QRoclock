package com.steveq.qroclock.repo;

import com.j256.ormlite.table.DatabaseTable;

public enum Days {
    MONDAY(2, "mon."),
    TUESDAY(3, "tue."),
    WEDNESDAY(4, "wed."),
    THURSDAY(5, "thu."),
    FRIDAY(6, "fri."),
    SATURDAY(7, "sat."),
    SUNDAY(1, "sun.");

    Integer dayNum;
    String abb;

    Days(Integer dayNum, String abb){
        this.dayNum = dayNum;
        this.abb = abb;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public String getAbb() {
        return abb;
    }
}
