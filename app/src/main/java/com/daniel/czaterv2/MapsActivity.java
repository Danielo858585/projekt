package com.daniel.czaterv2;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private LocationManager lm;
    private Double lat, lon;
    private Location location;
    private GPSManager gpsManager;
    private int checkPermissionLocalizationFine;
    private int checkPermissionLocalizationCoarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkPermissionLocalizationFine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermissionLocalizationCoarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        gpsManager = new GPSManager(checkPermissionLocalizationFine, checkPermissionLocalizationCoarse);
        gpsManager.checkPermissions();
        lat = gpsManager.getLatitude();
        lon = gpsManager.getLongitude();
        LatLng pozycja = new LatLng(lat,lon);

        if(pozycja != null){
            mMap.moveCamera();
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(pozycja));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        else {
            pozycja = new LatLng(51.236658,22.548534);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(pozycja,15)));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(pozycja));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Tutaj będzie centrum czatu"));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(1000)
                        .strokeColor(Color.RED)
                        .strokeWidth(1));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    //------------------- KONIEC OnMapReady ----------------------------------


}

