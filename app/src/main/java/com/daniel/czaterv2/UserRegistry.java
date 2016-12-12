package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class UserRegistry {

    private String email;
    private String login;
    private String password;

    public UserRegistry(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
