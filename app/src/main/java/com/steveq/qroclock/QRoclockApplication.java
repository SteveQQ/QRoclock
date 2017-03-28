package com.steveq.qroclock;


import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class QRoclockApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                        .setDefaultFontPath("fonts/Rubik-Regular.ttf")
                                        .build());
    }
}
