package com.daniel.czaterv2;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LocationManager lm;
    private Double lat, lon;
    private Location location;
    private GPSManager gpsManager;
    private int checkPermissionLocalizationFine;
    private int checkPermissionLocalizationCoarse;
    private Bundle bundle;
    private Intent intent;
    private CzatProperties czatProperties;
    private JSONObject jsonObject;
    private int czatRadius = 0;
    private CzatProperties czat1;
    private CzatProperties czat2;
    private CzatProperties czat3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkPermissionLocalizationFine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermissionLocalizationCoarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        intent = getIntent();
        bundle = new Bundle();
        Log.d("Maps_On_Create","a");
        bundle = intent.getExtras();
        if (bundle!=null){
            czatRadius = bundle.getInt("range");
        }
        else{
            czatRadius = 2000;
        }

        czat1 = new CzatProperties();
        czat1.setName("Czat 1");
        czat1.setRange(5000);
        czat1.setPosition(new LatLng(22.000,56.000));
        czat1.setMaxUsers(10);

        czat2 = new CzatProperties();
        czat2.setName("Czat 2");
        czat2.setRange(10000);
        czat2.setPosition(new LatLng(22.100,56.200));
        czat2.setMaxUsers(10);

        czat3 = new CzatProperties();
        czat3.setName("Czat 3");
        czat3.setRange(15000);
        czat3.setPosition(new LatLng(22.200,56.100));
        czat3.setMaxUsers(10);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pozycja2 = new LatLng(51.236658, 22.548534);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja2, 10));
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(czat1.getPosition())
                .title("Tutaj będzie centrum czatu"));
        Circle circle1 = mMap.addCircle(new CircleOptions()
                .center(czat1.getPosition())
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(czat2.getPosition())
                .title("Tutaj będzie centrum czatu"));
        Circle circle2 = mMap.addCircle(new CircleOptions()
                .center(czat2.getPosition())
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));
        Marker marker3 = mMap.addMarker(new MarkerOptions()
                .position(czat3.getPosition())
                .title("Tutaj będzie centrum czatu"));
        Circle circle3 = mMap.addCircle(new CircleOptions()
                .center(czat3.getPosition())
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (czatRadius == 0) {
                    czatRadius = 1000;
                }
                Log.d("Radius",String.valueOf(czatRadius));
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Tutaj będzie centrum czatu"));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(czatRadius)
                        .strokeColor(Color.RED)
                        .strokeWidth(3));
            }
        });
    }
    //------------------- KONIEC OnMapReady ----------------------------------
}

