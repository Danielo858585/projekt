package com.daniel.czaterv2;

/**
 * Created by Daniel on 13.09.2016.
 */
public class User {

    private String token;
    private String login;
    private String name;
    private String email;
    private String password;

    public User(){}

    public User(UserAnonymous userAnonymous) {
        name = userAnonymous.getName();
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public User(UserLoginResponse userLoginResponse){
        this.token = userLoginResponse.getToken();
        this.name = userLoginResponse.getName();
        this.email = userLoginResponse.getEmail();
    }

    public User(String login) {
        this.login = login;
    }

    ///////////////////////////////////////////////////////////////////////

    public String getLogin(){
        return this.login;
    }

    public void setLogin (String login){ this.login = login;    }

    public void setPassword(String password){
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
