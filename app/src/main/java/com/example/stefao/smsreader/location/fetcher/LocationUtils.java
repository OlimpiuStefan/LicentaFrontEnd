package com.example.stefao.smsreader.location.fetcher;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.stefao.smsreader.application.MessageApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by stefao on 4/1/2018.
 */

public class LocationUtils extends MessageApplication {
//    static FusedLocationProviderClient mFusedLocationClient;
//    static LocationManager locationManager;
//    Context context;
//    Activity activityCompat;
//
//    public LocationUtils(Context context, Activity activity) {
//        this.context = context;
//        this.activityCompat = activity;
//    }
//
//    public Task<Location> getLocation() {
//        //LocationListener locationListener = new MyLocationListener();
//        checkLocationPermission(this.activityCompat,this.context);
//        //locationManager = (LocationManager)
//           //     getSystemService(context);
//     //   locationManager = (LocationManager) this.activityCompat.getSystemService(Context.LOCATION_SERVICE);
////        locationManager.requestLocationUpdates(
////                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
//       // return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activityCompat);
//        return mFusedLocationClient.getLastLocation();
//    }

    static FusedLocationProviderClient mFusedLocationClient;
    static LocationManager locationManager;
    Context context;
    Activity activityCompat;

    public LocationUtils(Context context) {
        this.context = context;
    }

    public Task<Location> getLocation() {
        //LocationListener locationListener = new MyLocationListener();
        //checkLocationPermission(this.activityCompat,this.context);
        //locationManager = (LocationManager)
        //     getSystemService(context);
        //   locationManager = (LocationManager) this.activityCompat.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        // return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
        ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION);
        //int result = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1, 1);
        return mFusedLocationClient.getLastLocation();
    }
}
