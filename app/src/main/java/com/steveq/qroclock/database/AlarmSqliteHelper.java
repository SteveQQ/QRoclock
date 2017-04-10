package com.steveq.qroclock.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;

import java.sql.SQLException;

public class AlarmSqliteHelper extends OrmLiteSqliteOpenHelper{
    private static final String TAG = AlarmSqliteHelper.class.getSimpleName();
    private static final String DB_NAME = "alarms";
    private static final int DB_VERSION = 1;

    private Dao<Alarm, Integer> mAlarmDao;

    public Dao<Alarm, Integer> getAlarmDao() throws SQLException {
        if(mAlarmDao == null){
            mAlarmDao = getDao(Alarm.class);
        }
        return mAlarmDao;
    }

    public AlarmSqliteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            Log.d(TAG, "Creating Tables");
            TableUtils.createTable(connectionSource, Alarm.class);
            TableUtils.createTable(connectionSource, Day.class);
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
