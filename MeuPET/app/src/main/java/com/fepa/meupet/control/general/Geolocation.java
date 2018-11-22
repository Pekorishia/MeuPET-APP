package com.fepa.meupet.control.general;

import java.io.Serializable;

public class Geolocation implements Serializable {

    private double latitude;
    private double longitude;

    public Geolocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
