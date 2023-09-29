package com.app.activeparks.data.model.activity;

public class GeoPointEntity {
    double aLatitude;
    double aLongitude;

    public GeoPointEntity(double aLatitude, double aLongitude) {
        this.aLatitude = aLatitude;
        this.aLongitude = aLongitude;
    }

    public double getaLatitude() {
        return aLatitude;
    }

    public double getaLongitude() {
        return aLongitude;
    }
}
