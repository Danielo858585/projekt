package com.daniel.czaterv2;

import java.util.List;

/**
 * Created by Daniel on 30.12.2016.
 */

public class ChatDetailsResponse {
    String id;
    double latitude;
    double longitude;
    int maxUsersNumber;
    List<MessageResponse> messagesList;
    String name;
    int rangeInMeters;

    public ChatDetailsResponse(String id) {
        this.id = id;
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

    public List<MessageResponse> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageResponse> messagesList) {
        this.messagesList = messagesList;
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
