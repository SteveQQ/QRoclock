package com.steveq.qroclock.repo;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class RepoManager {
    private final String TAG = RepoManager.class.getSimpleName();

    private final String FILE_NAME = "alarms_log.json";
    private Context mContext;
    private Gson mGson;
    private File mFile;
    private Alarms dummyAlarms;

    public RepoManager(Context context) {
        mContext = context;
        mGson = new Gson();
        dummyAlarms = new Alarms(new ArrayList<Alarm>());
        createAlarmLog();
    }

    private boolean createAlarmLog(){
        mFile = new File(mContext.getFilesDir().getAbsolutePath(), FILE_NAME);
        if(!mFile.exists()) {
            try(FileWriter writer = new FileWriter(mFile)) {
                mGson.toJson(dummyAlarms,writer);
                Log.d(TAG, mGson.toJson(dummyAlarms));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public Alarms readAlarms(){
        Alarms alarms = dummyAlarms;
        try {
            Reader reader = new FileReader(mFile.getAbsolutePath());
            alarms = mGson.fromJson(reader, Alarms.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return alarms;
    }

    public void saveAlarm(Alarm a){
        Alarms alarms = readAlarms();
        alarms.getAlarms().add(a);
        try(FileWriter writer = new FileWriter(mFile)){
            mGson.toJson(alarms,writer);
            Log.d(TAG, mGson.toJson(alarms));
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
