package com.example.android_project;

import android.app.Application;
import android.util.Log;

import com.google.android.material.color.DynamicColors;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("MainApplication", String.valueOf(DynamicColors.isDynamicColorAvailable()));

        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
