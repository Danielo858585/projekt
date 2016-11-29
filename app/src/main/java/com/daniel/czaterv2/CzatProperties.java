package com.daniel.czaterv2;

/**
 * Created by Daniel on 18.11.2016.
 */

public class CzatProperties {
    private int idCzat;
    private int maxUsersNumber;
    private int range;
    private String name;
    private double longitude;
    private double latitude;

    public CzatProperties() {
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


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIdCzat() {
        return idCzat;
    }

    public void setIdCzat(int idCzat) {
        this.idCzat = idCzat;
    }
}
