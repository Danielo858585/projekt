package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class User {

    private int id;
    private int anonymusID;
    private String token;
    private String name;
    private String email;
    private String pass;



    public User(){}



    public User(String name, String pass){
        this.name = name;
        this.pass = pass;
    }

    public User(String name, String pass, String email){
        this.name = name;
        this.pass = pass;
        this.email = email;
    }

    public User(String name) {
        this.name = name;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAnonymusID() {
        return anonymusID;
    }

    public void setAnonymusID(int anonymusID) {
        this.anonymusID = anonymusID;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
