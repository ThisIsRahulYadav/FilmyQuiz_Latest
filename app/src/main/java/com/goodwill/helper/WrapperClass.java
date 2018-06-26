package com.goodwill.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.goodwill.filmyquiz.BackgroundSoundService;

/**
 * Created by lenovo on 1/4/2018.
 */


public class WrapperClass {

    Activity activity;


    private static WrapperClass wrapperClass;

    private WrapperClass(Activity activity) {
        //reference constructor created
        this.activity = activity;
    }

    public static WrapperClass getWrapperclass(Activity activity) {

        if (wrapperClass == null) {
            wrapperClass = new WrapperClass(activity);
        }
        return wrapperClass;
    }


    public void setsharedpreference(Activity activity, String key, int value) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
      //  return sharedPref.getInt(key, value);
    }


    public int getsharedpreference(Activity activity, String key, int defaultValue) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }


    public void stopSound(Activity activity) {


        Intent svc = new Intent(activity, BackgroundSoundService.class);
        activity.stopService(svc);
    }
    public void startSound(Activity activity) {

        Intent svc = new Intent(activity, BackgroundSoundService.class);
        activity.startService(svc);
    }


}