package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class UserRequest {

    private String name;
    private String email;
    private String pass;
    private int id;

    public UserRequest(String name, String email, String pass, int id) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
