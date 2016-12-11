package com.daniel.czaterv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static int LOGIN = 1;
    final static int REGISTRY = 2;
    private Button start;
    private Button gps_on;
    private Button go_to_map;
    private Button login;
    private Button registry;
    private Button checkGPSPosition;
    private LocationManager locationManager;
    private User user;
    private UserAnonymous userAnonymous;
    private String name;
    private String pass;
    private String userString;
    private GPSManager gpsManager;
    private AnonymousSend anonymousSend;
    private double lng = 0;
    private double lat = 0;
    private LocationRequest locationRequest;
    private Boolean mRequestingLocationUpdates;
    private TextView gps_info;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private GoogleApiClient googleApiClient;
    private int checkPermissionLocalizationFine;
    private int checkPermissionLocalizationCoarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionLocalizationFine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermissionLocalizationCoarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_info = (TextView) findViewById(R.id.tv_gpsinfo);
        latitudeTextView = (TextView) findViewById(R.id.tv_latitude);
        longitudeTextView = (TextView) findViewById(R.id.tv_longitude);
        start = (Button) findViewById(R.id.btn_main_start);
        gps_on = (Button) findViewById(R.id.btn_main_gps_enabled);
        login = (Button) findViewById(R.id.btn_login);
        registry = (Button) findViewById(R.id.btn_registry);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }

        createLocationRequest();

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registry();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == LOGIN) {
            if (resultCode == RESULT_OK) {
                user = App.getInstance().getUser();
                Log.d("Name", user.getLogin());
            } else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Logowanie nie powiodło się", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Log.d("Dupa", "Nie weszło w IFA w Main");
            }
        }
    }

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

    private void checkGPS() {
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

    private void registry() {
        Intent intent = new Intent(this, UserRegistry.class);
        startActivityForResult(intent, REGISTRY);
    }

    public void checkPermision() {
        int checkPermissionLocalizationFine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermissionLocalizationCoarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermissionLocalizationFine != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 777);
            }
        }
        if (checkPermissionLocalizationCoarse != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
            }
        }
    }

    // ---------------------- GOOGLE CLIENT API  ---------------------------
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
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
        Log.d("GoogleApiClient", "Metoda onConnected");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (mLastLocation != null) {
            latitudeTextView.setText(String.valueOf(mLastLocation.getLatitude()));
            longitudeTextView.setText(String.valueOf(mLastLocation.getLongitude()));
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

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(7000);
        locationRequest.setFastestInterval(3500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("CATCH", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
// ---------------------- END GOOGLE CLIENT API  ---------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 777: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast tost = Toast.makeText(this, "Uprawnienia FINE przyznane", Toast.LENGTH_SHORT);
                    tost.show();

                } else {

                    Toast tost = Toast.makeText(this, "Uprawnienia FINE nieprzyznane", Toast.LENGTH_SHORT);
                    tost.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 666: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast tost = Toast.makeText(this, "Uprawnienia COARSE przyznane", Toast.LENGTH_SHORT);
                    tost.show();
                } else {
                    Toast tost = Toast.makeText(this, "Uprawnienia COARSE nieprzyznane", Toast.LENGTH_SHORT);
                    tost.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
        checkPermision();
        AppIndex.AppIndexApi.start(googleApiClient, getIndexApiAction());

        if (App.getInstance().getUser() == null) {
            start.setText("Rozpocznij jako anonimowy");
        } else {
            String username = App.getInstance().getUser().getLogin();
            start.setText("Rozpocznij jako " + username);
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(googleApiClient, getIndexApiAction());
    }

}
