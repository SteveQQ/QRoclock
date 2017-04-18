package com.steveq.qroclock;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class QRoclockApplication extends Application {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                        .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                                        .build());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = QRoclockApplication.sharedPreferences.edit();
    }
}
