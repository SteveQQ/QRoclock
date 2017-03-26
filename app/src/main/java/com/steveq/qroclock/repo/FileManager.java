package com.steveq.qroclock.repo;


import android.content.Context;
import android.support.annotation.Nullable;
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

public class FileManager {
    private final String TAG = FileManager.class.getSimpleName();

    private final String FILE_NAME = "alarms_log.json";
    private Context mContext;
    private Gson mGson;
    private File mFile;
    private Alarm nullAlarm;
    private Alarms dummyAlarms;

    public FileManager(Context context) {
        mContext = context;
        mGson = new Gson();
        nullAlarm = new Alarm("00:00", false, new ArrayList<>(Arrays.asList(Days.MONDAY.name(), Days.FRIDAY.name())));
        dummyAlarms = new Alarms(new ArrayList<>(Arrays.asList(nullAlarm, nullAlarm)), "dummy object");
        createAlarmLog();
    }

    private boolean createAlarmLog(){
        mFile = new File(mContext.getFilesDir(), FILE_NAME);
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
}
