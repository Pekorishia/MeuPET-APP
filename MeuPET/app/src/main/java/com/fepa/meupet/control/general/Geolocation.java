package com.fepa.meupet.control.general;

import java.io.Serializable;

public class Geolocation implements Serializable {

    private String placeName;
    private double latitude;
    private double longitude;

    public Geolocation(String name, double latitude, double longitude) {
        this.placeName = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
