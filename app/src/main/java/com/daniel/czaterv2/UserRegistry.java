package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class UserRegistry {

    private String name;
    private String email;
    private String pass;

    public UserRegistry(){}

    public UserRegistry(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
