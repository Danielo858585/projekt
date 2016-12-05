package com.daniel.czaterv2;

/**
 * Created by Daniel on 18.11.2016.
 */

public class CzatProperties {


    private double latitude;
    private double longitude;
    private int range;
    private String name;
    private int maxUsersNumber;


    public CzatProperties() {
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

    public int getMaxUsers() {
        return maxUsersNumber;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsersNumber = maxUsers;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
