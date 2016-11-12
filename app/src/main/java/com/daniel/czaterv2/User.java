package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class User {

    private String name;
    private String email;
    private String pass;
    private int id;

    private boolean log = false;
    private boolean moderator = false;
    private boolean boss = false;


    public User(){}

    public User(String s){}

    public User(String name, String pass){
        this.name = name;
        this.pass = pass;
        log = false;
    }

    public User(String name, int id, String pass, String email){
        this.name = name;
        this.id = id;
        this.pass = pass;
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
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

    public void setLogOn(){
        log = true;
    }

    public void setLogOff(){
        log = false;
    }
}
