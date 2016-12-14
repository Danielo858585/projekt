package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCzatActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText czatName;
    private TextView maxUsersView, rangeView, czatPositionLongitude, czatPositionLatitude;
    private Button defineCzatLocation, acceptNewCzat;
    private CzatProperties czatProperties;
    private JSONObject jsonObject;
    private Boolean dataComplete;
    private SeekBar maxUsers, czatRange;
    private GoogleApiClient googleApiClient;
    int maxUsersInt, czatRangeInt;
    private double latitude, longitude;
    private Intent intent;
    private LocationRequest locationRequest;
    private static final int GET_CZAT_CENTER_INTENT = 2;
    protected static final int REQUEST_CHECK_SETTINGS = 1;

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
        czatPositionLatitude = (TextView) findViewById(R.id.tv_addNewChatLatitude);
        czatPositionLongitude = (TextView) findViewById(R.id.tv_addNewChatLongitude);
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
        createLocationRequest();

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
                    AddCzatRequest addCzatRequest = new AddCzatRequest();
                    addCzatRequest.setRange(czatRangeInt);
                    addCzatRequest.setName(czatName.getText().toString());
                    addCzatRequest.setMaxUsers(maxUsersInt);
                    addCzatRequest.setLongitude(longitude);
                    addCzatRequest.setLatitude(latitude);
                    final String headerInfo = App.getInstance().getUser().getToken();

                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    request = request.newBuilder()
                                            .addHeader("token", headerInfo)
                                            .build();
                                    okhttp3.Response response = chain.proceed(request);
                                    return response;
                                }
                            })
                            .addInterceptor(interceptor)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(App.getSendURL())
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .client(client)
                            .build();
                    WebService webService = retrofit.create(WebService.class);
                    Call<AddCzatResponse> call = webService.addCzat(addCzatRequest);
                    call.enqueue(new Callback<AddCzatResponse>() {
                        @Override
                        public void onResponse(Call<AddCzatResponse> call, Response<AddCzatResponse> response) {
                            Log.d("AddCzatActivity", response.toString());
                            Toast toast = Toast.makeText(getApplicationContext(),"Czat został pomyślnie dodany",Toast.LENGTH_LONG);
                            toast.show();

                        }

                        @Override
                        public void onFailure(Call<AddCzatResponse> call, Throwable t) {

                        }

                    });

                }
            }
        });
        intent = new Intent(this,CzatListActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == GET_CZAT_CENTER_INTENT) {
            if (resultCode == RESULT_OK) {
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
            czatPositionLongitude.setText("Longitude: " + String.valueOf(longitude));
            czatPositionLatitude.setText("Latitude: " + String.valueOf(latitude));

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
                            status.startResolutionForResult(AddCzatActivity.this, REQUEST_CHECK_SETTINGS);
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
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(googleApiClient, getIndexApiAction());
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
}
