package com.daniel.czaterv2;

/**
 * Created by Daniel on 02.12.2016.
 */

public class UserAnonymous extends User{
    public String name;
    public String token;

    public UserAnonymous(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
