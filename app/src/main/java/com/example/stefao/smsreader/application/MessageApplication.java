package com.example.stefao.smsreader.application;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by stefao on 4/1/2018.
 */

public class MessageApplication extends Application {

    public static int PERMISSION_ACCESS_FINE_LOCATION = 10;

    public boolean checkLocationPermission(Activity activity, Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
            System.out.println("in if---------------------------------------");
            return false;
        }
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, PersistService.class));
    }

}



