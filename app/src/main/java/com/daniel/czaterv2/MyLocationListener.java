package com.daniel.czaterv2;

import android.location.Location;
import android.util.Log;

/**
 * Created by Daniel on 25.10.2016.
 */

public class MyLocationListener implements com.google.android.gms.location.LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            double pLong = location.getLongitude();
            double pLat = location.getLatitude();
            Log.d("Pozycja",pLong + " " + pLat);
        }
    }
}
