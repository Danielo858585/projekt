package com.daniel.czaterv2;

/**
 * Created by Daniel on 18.11.2016.
 */

public class AddCzatResponse {

    private double latitude;
    private double longitude;
    private int rangeInMeters;
    private String name;
    private int maxUsersNumber;
    private int id;

    public AddCzatResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRangeInMeters() {
        return rangeInMeters;
    }

    public void setRangeInMeters(int rangeInMeters) {
        this.rangeInMeters = rangeInMeters;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
