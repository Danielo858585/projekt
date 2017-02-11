package com.daniel.czaterv2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static int LOGIN = 555;
    final static int NETWORK_PERMISSIONS = 444;
    final static int ACCESS_COARSE_LOCATION_PERMISSIONS = 666;
    final static int ACCESS_FINE_LOCATION_PERMISSIONS = 777;
    final static int REGISTRY = 2;
    private Button start, gps_on, login, registry, info;
    private LocationManager locationManager;
    private User user;
    private LocationRequest locationRequest;
    private TextView gps_info, latitudeTextView, longitudeTextView;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private GoogleApiClient googleApiClient;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_info = (TextView) findViewById(R.id.tv_gpsinfo);
        latitudeTextView = (TextView) findViewById(R.id.tv_latitude);
        longitudeTextView = (TextView) findViewById(R.id.tv_longitude);
        start = (Button) findViewById(R.id.btn_main_start);
        gps_on = (Button) findViewById(R.id.btn_main_gps_enabled);
        login = (Button) findViewById(R.id.btn_login);
        registry = (Button) findViewById(R.id.btn_registry);
        info = (Button) findViewById(R.id.btn_info);
        createGoogleApiClient();
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
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWelcomeScreen();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == LOGIN) {
            if (resultCode == RESULT_OK) {
                user = App.getInstance().getUser();
                Toast toast = Toast.makeText(getApplicationContext(), "Zostałeś poprawnie zalogowany", Toast.LENGTH_SHORT);
                toast.show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Logowanie nie powiodło się", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Log.d("Dupa", "Nie weszło w IFA w Main");
            }
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.d("MainActivity", "User agreed to make required location settings changes.");
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i("MainActivity", "User chose not to make required location settings changes.");
                    break;
            }
        }
    }

    public void clickStart() {
        Intent intent = new Intent(this, CzatListActivity.class);
        startActivity(intent);
    }

    private void gps_enable() {
        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(callGPSSettingIntent);
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    private void registry() {
        Intent intent = new Intent(this, RegistryActivity.class);
        startActivityForResult(intent, REGISTRY);
    }

    private void checkPermision() {
        int checkPermissionLocalizationFine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermissionLocalizationCoarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionInternet = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE);
        if (checkPermissionLocalizationFine != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSIONS);
            }
        }
        if (checkPermissionLocalizationCoarse != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_PERMISSIONS);
            }
        }
        if (checkPermissionInternet != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_NETWORK_STATE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, NETWORK_PERMISSIONS);
            }
        }
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

    private void setStartButtonEnabled() {
        if (App.getInstance().getMyPosition() == null || !checkInternetConnect()) {
            start.setEnabled(false);
        } else {
            start.setEnabled(true);
        }
    }

    private void createGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }
        App.getInstance().setGoogleApiClient(googleApiClient);
    }

    private void showWelcomeScreen() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Witaj").setMessage(getString(R.string.welcomeMessage)).setNeutralButton("OK", new zzh() {
            @Override
            protected void zzavx() {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkInternetConnect() {
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected)
            return true;
        else
            return false;
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            latitudeTextView.setText(String.valueOf(mLastLocation.getLatitude()));
            longitudeTextView.setText(String.valueOf(mLastLocation.getLongitude()));
            App.getInstance().setMyPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            setStartButtonEnabled();
        } else {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
            case 555: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast tost = Toast.makeText(this, "Uprawnienia NETWORK przyznane", Toast.LENGTH_SHORT);
                    tost.show();
                } else {
                    Toast tost = Toast.makeText(this, "Uprawnienia NETWORK nieprzyznane", Toast.LENGTH_SHORT);
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
        setStartButtonEnabled();
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
            String username = App.getInstance().getUser().getName();
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
