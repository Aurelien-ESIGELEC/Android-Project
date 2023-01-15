package com.example.android_project.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.android_project.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Utils {

    public static boolean isNetworkUnavailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
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

    public static Drawable createCustomIcon(Context context, int drawable, int color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color));

        return wrappedDrawable;
    }

    public static boolean isFieldEmpty(Context context, TextInputLayout et) {
        if (TextUtils.isEmpty(Objects.requireNonNull(et.getEditText()).getText())) {
            et.setError(context.getResources().getString(R.string.form_error_required));
            return true;
        }
        et.setError(null);
        return false;
    }

}
