package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.12.2016.
 */

public class CzatListRequest {
    double longitude;
    double latitude;

    public CzatListRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
