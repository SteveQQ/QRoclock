package com.steveq.qroclock.database;


import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmsManager {
    private static final String TAG = AlarmsManager.class.getSimpleName();
    private static AlarmsManager instance;
    private AlarmSqliteHelper dbHelper;

    //-----BOILERPLATE NOT INTERESTING CODE ;)-----//

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

    //-----BOILERPLATE NOT INTERESTING CODE ;)-----//

    //-----DB OPERATIONS CODE START-----//
    public int createAlarm(Alarm alarm){
        try {
            Log.d(TAG, "Creating Alarm : " + alarm);
            getHelper().getAlarmDao().create(alarm);
        } catch (SQLException e) {
            Log.d(TAG, "Creating Alarm FAILED");
            e.printStackTrace();
        }
        return alarm.getId();
    }

    public List<Alarm> readAlarms(){
        List<Alarm> results = new ArrayList<>();
        try {
            Log.d(TAG, "Reading Alarms List...");
            QueryBuilder<Alarm, Integer> queryBuilder = getHelper().getAlarmDao().queryBuilder();
            results = queryBuilder.query();
        } catch (SQLException e) {
            Log.d(TAG, "Reading Alarms List FAILED");
            e.printStackTrace();
        }
        return results;
    }

    public List<Alarm> readActiveAlarms(){
        List<Alarm> results = new ArrayList<>();
        try {
            Log.d(TAG, "Reading Alarms List...");
            QueryBuilder<Alarm, Integer> queryBuilder = getHelper().getAlarmDao().queryBuilder();
            queryBuilder.where().eq("active", true);
            results = queryBuilder.query();
        } catch (SQLException e) {
            Log.d(TAG, "Reading Alarms List FAILED");
            e.printStackTrace();
        }
        return results;
    }

    public Alarm readAlarmById(Integer id){
        Alarm result = null;
        try {
            Log.d(TAG, "Reading Alarm by Id : " + id);
            result = getHelper().getAlarmDao().queryForId(id);
        } catch (SQLException e) {
            Log.d(TAG, "Reading Alarm by Id FAILED");
            e.printStackTrace();
        }
        Log.d(TAG, "Alarm read : " + result);
        return result;
    }

    public void updateAlarmTime(Alarm alarm){
        try{
            Log.d(TAG, "Updating Alarm TIME : " + alarm);
            UpdateBuilder<Alarm, Integer> updateBuilder = getHelper().getAlarmDao().updateBuilder();
            updateBuilder.where().eq("id", alarm.getId());
            updateBuilder.updateColumnValue("time", alarm.getTime());
            updateBuilder.update();
        } catch (SQLException e){
            Log.d(TAG, "Updating Alarm Time FAILED");
            e.printStackTrace();
        }
    }

    public void updateAlarmActiveStatus(Alarm alarm){
        try{
            Log.d(TAG, "Updating Alarm Active Status : " + alarm);
            UpdateBuilder<Alarm, Integer> updateBuilder = getHelper().getAlarmDao().updateBuilder();
            updateBuilder.where().eq("id", alarm.getId());
            updateBuilder.updateColumnValue("active", alarm.getActive());
            updateBuilder.update();
        }catch (SQLException e){
            Log.d(TAG, "Updating Alarm Active Status FAILED");
            e.printStackTrace();
        }
    }

    public void updateAlarmRingtoneUri(Alarm alarm){
        try{
            Log.d(TAG, "Updating Alarm Ringtone Uri" + alarm);
            UpdateBuilder<Alarm, Integer> updateBuilder = getHelper().getAlarmDao().updateBuilder();
            updateBuilder.where().eq("id", alarm.getId());
            updateBuilder.updateColumnValue("ringtoneUri", alarm.getRingtoneUri());
            updateBuilder.update();
        } catch (SQLException e){
            Log.d(TAG, "Updating Alarm Ringtone Uri FAILED");
            e.printStackTrace();
        }
    }

    public void updateAlarmDays(Alarm alarm, ForeignCollection<Day> daysToUpdate){
        try{
            Log.d(TAG, "Updating Alarm Repetition Days");
            UpdateBuilder<Alarm, Integer> updateBuilder = getHelper().getAlarmDao().updateBuilder();
            updateBuilder.where().eq("id", alarm.getId());
            alarm.getDays().clear();
            alarm.getDays().updateAll();
            for(Day d : daysToUpdate){
                alarm.getDays().add(d);
            }
        } catch (SQLException e){
            Log.d(TAG, "Updating Alarm Repetition Days FAILED");
            e.printStackTrace();
        }
        Log.d(TAG, "Updated days" + alarm.getDays());
    }

    public void deleteAlarm(Alarm alarm){
        try{
            Log.d(TAG, "Deleting Alarm : " + alarm);
            DeleteBuilder<Alarm, Integer> deleteBuilder = getHelper().getAlarmDao().deleteBuilder();
            deleteBuilder.where().eq("id", alarm.getId());
            deleteBuilder.delete();
        } catch (SQLException e){
            Log.d(TAG, "Deleting Alarm FAILED");
            e.printStackTrace();
        }
    }

    //-----DB OPERATIONS CODE END-----//
}
