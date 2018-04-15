package com.example.stefao.smsreader;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;


public class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("LONGITUDE", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("LATITUDE", latitude);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}