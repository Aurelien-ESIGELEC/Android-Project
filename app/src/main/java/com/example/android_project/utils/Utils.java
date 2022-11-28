package com.example.android_project.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;

import com.example.android_project.R;
import com.google.android.material.snackbar.Snackbar;

public class Utils {

    public static boolean isNetworkUnavailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return !(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }

    public static Snackbar createSnackbarNoNetwork(View view, View.OnClickListener listener) {
        return Snackbar.make(
                view,
                R.string.app_error_no_network,
                Snackbar.LENGTH_LONG
        ).setAction(
                R.string.app_retry_action,
                listener
        );
    }

}
