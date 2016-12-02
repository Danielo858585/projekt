package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class UserRegistry {

    private String login;
    private String email;
    private String password;

    public UserRegistry(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getName(){
        return this.login;
    }

    public void setName(String login){
        this.login = login;
    }

    public void setPass(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return password;
    }
}
