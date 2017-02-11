package com.daniel.czaterv2;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private static App instance;

    private static String sendURL, endpoint;

    private User user;

    private UserAnonymous userAnonymous;

    private CzatProperties czatProperties;

    private LatLng myPosition;

    private List<CzatListResponseDetails> czatListResponseDetailses = new ArrayList<>();

    public static volatile Handler applicationHandler = null;

    public GoogleApiClient googleApiClient;

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

    public CzatProperties getCzatProperties() {
        return czatProperties;
    }

    public void setCzatProperties(CzatProperties czatProperties) {
        this.czatProperties = czatProperties;
    }

    public LatLng getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(LatLng myPosition) {
        this.myPosition = myPosition;
    }

    public List<CzatListResponseDetails> getCzatListResponseDetailses() {
        return czatListResponseDetailses;
    }

    public void setCzatListResponseDetailses(List<CzatListResponseDetails> czatListResponseDetailses) {
        this.czatListResponseDetailses = czatListResponseDetailses;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public static String getEndpoint() {
        return endpoint;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        applicationHandler = new Handler(getInstance().getMainLooper());
        NativeLoader.initNativeLibs(App.getInstance());
        sendURL = getResources().getString(R.string.IPSend);
        endpoint = getResources().getString(R.string.endpoint);
    }
}
