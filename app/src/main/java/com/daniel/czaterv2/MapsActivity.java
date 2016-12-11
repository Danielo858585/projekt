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
    private Marker marker;
    int i = 0;




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
        czat1.setLatitude(51.227863);
        czat1.setLongitude(22.434850);
        czat1.setMaxUsers(10);

        czat2 = new CzatProperties();
        czat2.setName("Czat 2");
        czat2.setRange(10000);
        czat2.setLatitude(51.228430);
        czat2.setLongitude(22.527721);
        czat2.setMaxUsers(10);

        czat3 = new CzatProperties();
        czat3.setName("Czat 3");
        czat3.setRange(15000);
        czat2.setLatitude(51.285379);
        czat2.setLongitude(22.598139);
        czat3.setMaxUsers(10);
    }

    @Override
    public void onBackPressed(){
        CzatProperties czatProperties = new CzatProperties();
        czatProperties.setLatitude(marker.getPosition().latitude);
        czatProperties.setLongitude(marker.getPosition().longitude);
        App.getInstance().setCzatProperties(czatProperties);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        onBackPressed();
        mMap = googleMap;
        LatLng pozycja2 = new LatLng(51.236658, 22.548534);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja2, 10));
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(czat1.getLatitude(),czat1.getLongitude()))
                .title("Tutaj będzie centrum czatu"));
        Circle circle1 = mMap.addCircle(new CircleOptions()
                .center(new LatLng(czat1.getLatitude(),czat1.getLongitude()))
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(czat2.getLatitude(),czat2.getLongitude()))
                .title("Tutaj będzie centrum czatu"));
        Circle circle2 = mMap.addCircle(new CircleOptions()
                .center(new LatLng(czat2.getLatitude(),czat2.getLongitude()))
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));
        Marker marker3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(czat3.getLatitude(),czat3.getLongitude()))
                .title("Tutaj będzie centrum czatu"));
        Circle circle3 = mMap.addCircle(new CircleOptions()
                .center(new LatLng(czat3.getLatitude(),czat3.getLongitude()))
                .radius(czatRadius)
                .strokeColor(Color.RED)
                .strokeWidth(3));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(i>=1){
                    mMap.clear();
                    i=0;
                }

                if (czatRadius == 0) {
                    czatRadius = 1000;
                }
                Log.d("Radius",String.valueOf(czatRadius));

                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Tutaj będzie centrum czatu"));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(czatRadius)
                        .strokeColor(Color.RED)
                        .strokeWidth(3));
                i++;
            }
        });
    }

    //------------------- KONIEC OnMapReady ----------------------------------

}

