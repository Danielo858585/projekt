package com.daniel.czaterv2;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App instance;

    private User user;

    private UserAnonymous userAnonymous;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAnonymous getUserAnonymous() {
        return userAnonymous;
    }

    public void setUserAnonymous(UserAnonymous userAnonymous) {
        this.userAnonymous = userAnonymous;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
