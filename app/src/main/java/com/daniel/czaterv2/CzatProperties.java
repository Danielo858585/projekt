package com.daniel.czaterv2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 18.11.2016.
 */

public class CzatProperties {
    private int maxUsersNumber;
    private int range;
    private String name;
    private LatLng position;

    public CzatProperties() {
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
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
