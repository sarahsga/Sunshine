package com.example.sarahahmed.sunshine;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sarah Ahmed on 2/7/2015.
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
