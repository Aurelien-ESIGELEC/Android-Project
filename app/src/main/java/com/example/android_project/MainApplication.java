package com.example.android_project;

import android.app.Application;
import android.util.Log;

import com.google.android.material.color.DynamicColors;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
