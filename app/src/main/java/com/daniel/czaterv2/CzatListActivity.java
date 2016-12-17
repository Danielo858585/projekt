package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CzatListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private String macAddress;
    static SecureRandom rnd = new SecureRandom();
    private TextView tv_user_name;
    private EditText et_user_name;
    private ListView chatList;
    private Button czat_list_accept;
    private Button sprawdz;
    private Button addCzat;
    private User user;
    private WebService webService;
    private UserAnonymous userAnonymous1;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private CzatListResponseDetails czatListResponseDetails;
    private double latitude, longitude;
    private List<CzatListResponseDetails> listChats = new ArrayList<>();
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private CzatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czat_list);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        chatList = (ListView) findViewById(R.id.lv_czat_list);
        czat_list_accept = (Button) findViewById(R.id.btn_accept_czat_list);
        addCzat = (Button) findViewById(R.id.btn_addCzat);
        getAnonymousUserName();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }

        adapter = new CzatListAdapter(CzatListActivity.this, listChats);
        chatList.setAdapter(adapter);

        czat_list_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_on_click();
            }
        });
        addCzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCzatOnClick();
            }
        });
    }

    private void addCzatOnClick() {
        Intent intent = new Intent(this, AddCzatActivity.class);
        startActivity(intent);
    }

    // // // // // // // ZATWIERDZENIE WYBRANEGO CZATU
    private void button_on_click() {
        User user = new User(et_user_name.getText().toString());
        Intent intent = new Intent(this, CzatActivity.class);
        String message = et_user_name.getText().toString();
        intent.putExtra("message", message);
        startActivity(intent);
    }
    // ** ** ** ** ** ** ** ** ** ** ** **

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private void getMacAdress() {
        // POBRANIE ADRESU MAC URZĄDZENIA
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        macAddress = info.getMacAddress();
    }

    private void buildRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.getSendURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        webService = retrofit.create(WebService.class);
    }

    private void getAnonymousUserName() {
        buildRetrofit();
        getMacAdress();

        try {
            final Call<UserAnonymous> userAnonymous = webService.userAnonymous(new AnonymousSend(macAddress));
            Log.d("MainActivity", "TRY");
            userAnonymous.enqueue(new Callback<UserAnonymous>() {
                @Override
                public void onResponse(Call<UserAnonymous> call, Response<UserAnonymous> response) {
                    Log.d("CzatListActivity", "ON RESPONSE");
                    Log.d("CzatListActivity", response.toString());
                    userAnonymous1 = response.body();
                    User user = new User(userAnonymous1);
                    App.getInstance().setUser(user);
                    Log.d("UserAnonymous name", userAnonymous1.name);
                    Log.d("UserAnonymous token", userAnonymous1.token);
                    et_user_name.setText(App.getInstance().getUser().getName().toString());
                }

                @Override
                public void onFailure(Call<UserAnonymous> call, Throwable t) {
                    Log.d("MainActivity", "ON FAILURE");
                    Log.d("ON FAILURE", call.toString());
                    Log.d("ON FAILURE", t.toString());
                }
            });

        } catch (Exception e) {
            Log.d("MainActivity", e.toString());
            userAnonymous1.setName("Nie pobrano na podstawie adresu MAC");
            userAnonymous1.setToken("Jakiś token");
        }
    }

    private void getCzatList() {
        buildRetrofit();
        Call<Chats> getCzatList = webService.getChatList(new CzatListRequest(latitude, longitude));
        getCzatList.enqueue(new Callback<Chats>() {
            @Override
            public void onResponse(Call<Chats> call, Response<Chats> response) {
                Chats chats = response.body();
                Log.d("CzatListActivity", "Response");
                listChats.clear();
                listChats.addAll(chats.getChats());
                adapter.notifyDataSetChanged();

                if (listChats.isEmpty()) {
                    Log.d("CzatListActivity", "Brak elementów");
                } else {
                    Log.d("CzatListActivity", listChats.toString());
                }
            }

            @Override
            public void onFailure(Call<Chats> call, Throwable t) {
                Log.d("CzatListActivity", "Failure");
            }
        });
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList.);
//        chatList.setAdapter(arrayAdapter);


//        buildRetrofit();
//        final Call<List<CzatListResponseDetails>> getCzatList = webService.getChatList(new CzatListRequest(latitude, longitude));
//        getCzatList.enqueue(new Callback<List<CzatListResponseDetails>>() {
//            @Override
//            public void onResponse(Call<List<CzatListResponseDetails>> call, Response<List<CzatListResponseDetails>> response) {
//                Log.d("CzatListActivity","Response");
//                listChats = response.body();
//                Log.d("CzatListActivity",listChats.toString());
//                int listSize = listChats.size();
//                String[] nazwy = new String[listSize];
//                for (int i=0;i<=listSize;i++){
//                    nazwy[i] = listChats.get(i).getName();
//                    Log.d("CzatListActivity -Nazwy",nazwy[i]);
//                }
//
//                arrayList.addAll(listChats);
//                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.my_simple_list_view,arrayList);
//                chatList.setAdapter(arrayAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<CzatListResponseDetails>> call, Throwable t) {
//                Log.d("CzatListActivity","Failure");
//            }
//        });
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList.);
//        chatList.setAdapter(arrayAdapter);


//        buildRetrofit();
//        Call<CzatListResponseDetails> getCzatList = webService.getChatList(new CzatListRequest(latitude, longitude));
//        getCzatList.enqueue(new Callback<CzatListResponseDetails>() {
//            @Override
//            public void onResponse(Call<CzatListResponseDetails> call, Response<CzatListResponseDetails> response) {
//                Log.d("CzatListActivity", "Response");
//                CzatListResponseDetails czatListResponseDetails = response.body();
//
//                if (czatListResponseDetails.getName() != null) {
//                    Log.d("CzatListActivity", czatListResponseDetails.getName());
//                } else {
//                    Log.d("CzatListActivity", "Response is null");
//                }
//                Log.d("CzatListActivity",listChats.toString());
//                int listSize = listChats.size();
//                String[] nazwy = new String[listSize];
//                for (int i=0;i<=listSize;i++){
//                    nazwy[i] = listChats.get(i).getName();
//                    Log.d("CzatListActivity -Nazwy",nazwy[i]);
//                }
//
//                arrayList.addAll(listChats);
//                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.my_simple_list_view,arrayList);
//                chatList.setAdapter(arrayAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<CzatListResponseDetails> call, Throwable t) {
//
//            }
//        });
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList.);
//        chatList.setAdapter(arrayAdapter);
    }

    protected void addExampleChats() {
        CzatProperties czat1;
        CzatProperties czat2;
        CzatProperties czat3;

        czat1 = new CzatProperties();
        czat1.setName("Czat 1");
        czat1.setRange(5000);
        czat1.setMaxUsers(10);

        czat2 = new CzatProperties();
        czat2.setName("Czat 2");
        czat2.setRange(10000);
        czat2.setMaxUsers(10);

        czat3 = new CzatProperties();
        czat3.setName("Czat 3");
        czat3.setRange(15000);
        czat3.setMaxUsers(10);
    }

    protected void createLocationRequest() {
        Log.d("CzatListActivity - clr", "CzatListActivity");
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
                            status.startResolutionForResult(CzatListActivity.this, REQUEST_CHECK_SETTINGS);
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
        getCzatList();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
        checkPermision();
        createLocationRequest();
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
            Toast toast = Toast.makeText(getApplicationContext(), "Longitude: " + String.valueOf(longitude), Toast.LENGTH_LONG);
            Toast toast2 = Toast.makeText(getApplicationContext(), "Latitude: " + String.valueOf(latitude), Toast.LENGTH_LONG);
            toast.show();
            toast2.show();

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
