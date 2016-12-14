package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.12.2016.
 */

public class CzatListResponse {
    String id;
    double latitude;
    double longitude;
    int maxUsersNumber;
    String name;
    int rangeInMeters;

    public CzatListResponse(String id, double latitude, double longitude, int maxUsersNumber, String name, int rangeInMeters) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxUsersNumber = maxUsersNumber;
        this.name = name;
        this.rangeInMeters = rangeInMeters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getMaxUsersNumber() {
        return maxUsersNumber;
    }

    public void setMaxUsersNumber(int maxUsersNumber) {
        this.maxUsersNumber = maxUsersNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRangeInMeters() {
        return rangeInMeters;
    }

    public void setRangeInMeters(int rangeInMeters) {
        this.rangeInMeters = rangeInMeters;
    }
}
