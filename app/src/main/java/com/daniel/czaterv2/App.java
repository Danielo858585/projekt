package com.daniel.czaterv2;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class App extends Application {

    private static App instance;

    private static final String sendURL = "http://192.168.0.2:8080";

    private User user;

    private UserAnonymous userAnonymous;

    public static volatile Handler applicationHandler = null;

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

    public static String getSendURL() {
        return sendURL;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        applicationHandler = new Handler(getInstance().getMainLooper());

        NativeLoader.initNativeLibs(App.getInstance());
    }
}
