package com.example.stefao.smsreader.utils.location;

/**
 * Created by stefao on 4/12/2018.
 */

public class UserLocation {
    private double latitude;
    private double longitude;
    private boolean locationSetted = false;

    public UserLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserLocation(){};

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isLocationSetted() {
        return locationSetted;
    }

    public void setLocationSetted(boolean locationSetted) {
        this.locationSetted = locationSetted;
    }
}
