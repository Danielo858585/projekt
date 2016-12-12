package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

public class AddCzatActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText czatName;
    private TextView maxUsersView, rangeView, czatPosition;
    private Button defineCzatLocation, acceptNewCzat;
    private CzatProperties czatProperties;
    private JSONObject jsonObject;
    private Boolean dataComplete;
    private SeekBar maxUsers, czatRange;
    private GoogleApiClient googleApiClient;
    int maxUsersInt, czatRangeInt;
    private double latitude, longitude;
    private Intent intent;
    private static final int GET_CZAT_CENTER_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_czat);

        czatName = (EditText) findViewById(R.id.et_czatName);
        maxUsers = (SeekBar) findViewById(R.id.sb_maxUsers);
        czatRange = (SeekBar) findViewById(R.id.sb_range);
        defineCzatLocation = (Button) findViewById(R.id.btn_defineCzatCenter);
        acceptNewCzat = (Button) findViewById(R.id.btn_acceptNewCzat);
        maxUsersView = (TextView) findViewById(R.id.tv_sbMaxUsersView);
        rangeView = (TextView) findViewById(R.id.tv_sbRangeView);
        czatPosition = (TextView) findViewById(R.id.tv_addNewChatPosition);
        jsonObject = new JSONObject();
        maxUsersInt = 2;
        czatRangeInt = 1000;
        maxUsers.setLeft(1);
        maxUsers.setRight(10);
        czatRange.setLeft(1);
        czatRange.setRight(10000);
        czatRange.setProgress(100);
        intent = getIntent();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }

        maxUsers.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxUsersInt = progress;
                maxUsersView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        czatRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                czatRangeInt = progress;
                rangeView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        defineCzatLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String czatNameString = czatName.getText().toString();
//
//                if (czatNameString.length() <= 0) {
//                    Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz nazwe czatu", Toast.LENGTH_SHORT);
//                    tost.show();
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                    intent.putExtra("range", czatRangeInt);
//                    startActivityForResult(intent, GET_CZAT_CENTER_INTENT);
//                }
//            }
//        });

        acceptNewCzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String czatNameString = czatName.getText().toString();
                czatRangeInt = czatRange.getProgress();
                maxUsersInt = maxUsers.getProgress();
                if (czatNameString.length() <= 0) {
                    Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz nazwe czatu", Toast.LENGTH_SHORT);
                    tost.show();
                } else {
                    CzatProperties czatProperties = new CzatProperties();
                    czatProperties.setRange(czatRangeInt);
                    czatProperties.setName(czatName.getText().toString());
                    czatProperties.setMaxUsers(maxUsersInt);
                    czatProperties.setLongitude(longitude);
                    czatProperties.setLatitude(latitude);


                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode==GET_CZAT_CENTER_INTENT){
            if (resultCode==RESULT_OK){
                intent = getIntent();
                intent.getExtras();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("AddCzatActivity", "Metoda onConnected - GAC");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            czatPosition.setText("Twoja pozycja to: \nlatitude - " + latitude + "\nlongitude - " + longitude);
        } else {
            Log.d("Else w OnConnected", "Ostatnia znana jest nie znana. ");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
