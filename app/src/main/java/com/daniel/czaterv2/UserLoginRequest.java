package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class UserLoginRequest {

    private String loginOrEmail;
    private String password;

    public UserLoginRequest(String password, String loginOrEmail) {
        this.password = password;
        this.loginOrEmail = loginOrEmail;
    }

    public String getLoginOrEmail() {
        return loginOrEmail;
    }

    public void setLoginOrEmail(String loginOrEmail) {
        this.loginOrEmail = loginOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
