package com.daniel.czaterv2;

/**
 * Created by Daniel on 22.10.2016.
 */
public class MySingleton {
    private static MySingleton ourInstance = null;

    private User user;
    private String name;

    public MySingleton(){
        user = new User();
    }

    public static MySingleton getInstance() {
        if(ourInstance == null) {
            ourInstance = new MySingleton();
        }
        return ourInstance;
    }

    public static boolean isNull(){
        if(ourInstance==null){
            return true;
        }
        else
            return false;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
