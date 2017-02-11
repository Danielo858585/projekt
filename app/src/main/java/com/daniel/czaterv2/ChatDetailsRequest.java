package com.daniel.czaterv2;

/**
 * Created by Daniel on 30.12.2016.
 */

public class ChatDetailsRequest {
    private String id;

    public ChatDetailsRequest() {
    }

    public ChatDetailsRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
