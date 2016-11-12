package com.daniel.czaterv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.ButterKnife;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static int LOGIN = 1;

    // private TextView autors;
    //private TextView gps_info;
    private Button start;
    private Button gps_on;
    private Button go_to_map;
    private Button login;
    private Button checkGPSPosition;
    private LocationManager locationManager;
    private User user;
    private String name;
    private String pass;
    private String userString;
    //private GPSManager gps;
    private double lng = 0;
    private double lat = 0;

    private TextView gps_info;
    private TextView latitudeTextView;
    private TextView longitudeTextView;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_info = (TextView) findViewById(R.id.tv_gpsinfo);
        latitudeTextView = (TextView) findViewById(R.id.tv_latitude);
        longitudeTextView = (TextView) findViewById(R.id.tv_longitude);
        start = (Button) findViewById(R.id.btn_main_start);
        gps_on = (Button) findViewById(R.id.btn_main_gps_enabled);
        go_to_map = (Button) findViewById(R.id.btn_map);
        login = (Button) findViewById(R.id.btn_loginRegistry);
        checkGPSPosition = (Button) findViewById(R.id.btn_checkGPSPosition);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStart();
            }
        });
        gps_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps_enable();
            }
        });
        go_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapy();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        checkGPSPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Log.d("Jestem w OnClick","Jestem w OnClick");
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Location location = new Location(LocationManager.NETWORK_PROVIDER);
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Random r = new Random();
                            int i1 = r.nextInt(80 - 25) + 65;
                            lat = location.getLatitude();
                            lng = location.getLongitude();

                            if((lat != 0) && (lng != 0)){
                                longitudeTextView.setText(String.valueOf(lng));
                                latitudeTextView.setText(String.valueOf(lat));
                            }
                            else{
                                longitudeTextView.setText(R.string.noGPSSignal);
                                longitudeTextView.setBackgroundColor(i1);
                                latitudeTextView.setText(R.string.noGPSSignal);
                                latitudeTextView.setBackgroundColor(i1);
                            }
                        }
                    };

                }
                else{
                    Log.d("1","Nie działa metoda po kliknięciu");
                }

                //gps = new GPSManager(getApplicationContext());
*/
            }
        });
        chceckGPS();
    }

    /*GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */
    /*                this /* OnConnectionFailedListener */
    /*        .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .build();
    */

    /*if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if (resultCode == RESULT_OK) {
            // sprawdzamy czy przyszło odpowiednie żądanie
            if (requestCode == 1) {
                /*
                Bundle bundle = intent.getExtras();
                name = bundle.getString("name");
                pass = bundle.getString("pass");
                userString = bundle.getString("USERjson");
                Log.d("Name", name);
                Log.d("Password", pass);
                user = new Gson().fromJson(userString, User.class);
                */
                user = MySingleton.getInstance().getUser();
                Log.d("Name", user.getName());
                //Log.d("Password", pass);
            } else {
                Log.d("Name", user.getName());
                //Log.d("Password", pass);
            }
        }
        else{
            Log.d("Dupa","Nie weszło w IFA w Main");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chceckGPS();
    }

    //SHOW GPS STATUS
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Włącz GPS")
                .setCancelable(false)
                .setPositiveButton("Przejdź do ustawień",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void clickStart() {
        Intent intent = new Intent(this, CzatListActivity.class);
        startActivity(intent);
    }

    private void gps_enable() {
        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(callGPSSettingIntent);
    }

    private void mapy() {
        Intent mapy = new Intent(this, MapsActivity.class);
        startActivity(mapy);
    }

    private void chceckGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS włączony", Toast.LENGTH_SHORT).show();
            gps_info.setText("GPS ON");
            start.setClickable(true);
            start.setText("Rozpocznij");
            start.setEnabled(true);
        } else {
            showGPSDisabledAlertToUser();
            gps_info.setText("GPS OFF");
            start.setClickable(false);
            start.setText("Włącz GPS");
            start.setEnabled(false);
        }
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
       /* Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
