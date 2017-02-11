package com.daniel.czaterv2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<CzatListResponseDetails> czatListResponseDetailses = new ArrayList<>();
    private LatLng myPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        czatListResponseDetailses = App.getInstance().getCzatListResponseDetailses();
        myPosition = App.getInstance().getMyPosition();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                String id = marker.getId();
                Intent intent = new Intent(getApplicationContext(),CzatActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 10));


        for (int i = 0; i<czatListResponseDetailses.size();i++){
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(czatListResponseDetailses.get(i).getLatitude(),czatListResponseDetailses.get(i).getLongitude()))
                    .title(czatListResponseDetailses.get(i).getName()));
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(czatListResponseDetailses.get(i).getLatitude(),czatListResponseDetailses.get(i).getLongitude()))
                    .radius(czatListResponseDetailses.get(i).getRangeInMeters())
                    .strokeColor(Color.RED)
                    .strokeWidth(5));
        }
    }
}

