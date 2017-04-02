package com.steveq.qroclock.database;


import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.steveq.qroclock.repo.Alarm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmsManager {
    private static AlarmsManager instance;
    private AlarmSqliteHelper dbHelper;

    private AlarmsManager(Context ctx){
        this.dbHelper = OpenHelperManager.getHelper(ctx, AlarmSqliteHelper.class);
    }

    public static AlarmsManager getInstance(Context ctx){
        if(instance == null){
            instance = new AlarmsManager(ctx);
        }
        return instance;
    }

    private AlarmSqliteHelper getHelper(){return dbHelper;}

    public int createAlarm(Alarm alarm){
        try {
            getHelper().getAlarmDao().create(alarm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarm.getId();
    }

    public List<Alarm> readAlarms(){
        List<Alarm> results = new ArrayList<>();
        try {
            QueryBuilder<Alarm, Integer> queryBuilder = getHelper().getAlarmDao().queryBuilder();
            results = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Alarm readAlarmById(Integer id){
        Alarm result = null;
        try {
            result = getHelper().getAlarmDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateAlarm(Alarm alarm){
        try {
            UpdateBuilder<Alarm, Integer> updateBuilder = getHelper().getAlarmDao().updateBuilder();
            updateBuilder.where().eq("id", alarm.getId());
            updateBuilder.updateColumnValue("time", alarm.getTime());
            updateBuilder.updateColumnValue("active", alarm.getActive());
            updateBuilder.updateColumnValue("ringtoneUri", alarm.getRingtoneUri());
            updateBuilder.updateColumnValue("days", alarm.getDays());
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
