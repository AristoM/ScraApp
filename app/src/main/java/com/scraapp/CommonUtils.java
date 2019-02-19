package com.scraapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aristomichael on 26/09/18.
 */

class CommonUtils {

    class LocationConstants {

        static final int SUCCESS_RESULT = 0;
        static final int FAILURE_RESULT = 1;
        static final String PACKAGE_NAME = "com.sample.sishin.maplocation";
        static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
        static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";

    }

    static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    static boolean textValidation(TextView textView) {
        return !TextUtils.isEmpty(textView.getText().toString());
    }

    static boolean textCompare(TextView textView1, TextView textView2) {
        return textView1.getText().toString().equalsIgnoreCase(textView2.getText().toString());
    }

    static void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    static void saveSharedPref(String prefFileName, String key, String value) {
        SharedPreferences sp = getSharedpref(prefFileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();

    }

    private static SharedPreferences getSharedpref(String prefFileName) {
        return ScrApp.getApp().getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    static String getSharedPref(String prefFileName, String key) {
        SharedPreferences sp = getSharedpref(prefFileName);
        return sp.getString(key, null);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
