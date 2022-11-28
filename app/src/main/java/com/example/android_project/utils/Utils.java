package com.example.android_project.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

    public static boolean isNetworkUnavailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected();
    }
}
