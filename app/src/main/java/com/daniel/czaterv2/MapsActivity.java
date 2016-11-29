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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pozycja2 = new LatLng(51.236658, 22.548534);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja2, 10));
//        gpsManager = new GPSManager(checkPermissionLocalizationFine, checkPermissionLocalizationCoarse);
//        //gpsManager.checkPermissions();
//        lat = gpsManager.getLatitude();
//        lon = gpsManager.getLongitude();
//        LatLng pozycja = new LatLng(lat, lon);
//
//        if (pozycja != null) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja, 5));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        }
// else {
//            pozycja = new LatLng(51.236658, 22.548534);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pozycja, 5));
//            //mMap.moveCamera(CameraUpdateFactory.newLatLng(pozycja));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        }

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

//    @Override
//    public void onLocationChanged(Location location) {
//
//    }

    //------------------- KONIEC OnMapReady ----------------------------------
}

