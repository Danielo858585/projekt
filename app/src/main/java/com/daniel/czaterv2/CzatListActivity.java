package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CzatListActivity extends Activity {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private String macAddress;
    static SecureRandom rnd = new SecureRandom();
    private TextView tv_user_name;
    private EditText et_user_name;
    private ListView czat_list;
    private Button czat_list_accept;
    private Button sprawdz;
    private Button addCzat;
    private User user;
    private CzatProperties czat1;
    private CzatProperties czat2;
    private CzatProperties czat3;
    private WebService webService;
    private UserAnonymous userAnonymous1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czat_list);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        czat_list = (ListView) findViewById(R.id.lv_czat_list);
        czat_list_accept = (Button) findViewById(R.id.btn_accept_czat_list);
        addCzat = (Button) findViewById(R.id.btn_addCzat);
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

        ArrayList<String> listItem = new ArrayList<String>();
        listItem.add(czat1.getName());
        listItem.add(czat2.getName());
        listItem.add(czat3.getName());

        czat_list.setAdapter(new ArrayAdapter<String>(this,R.layout.my_simple_list_view,listItem));
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

        if(App.getInstance().getUser()==null){
            getAnonymousUserName();

        }
        else{
            et_user_name.setText(App.getInstance().getUser().getLogin().toString());
        }
    }

    private void addCzatOnClick() {
        Intent intent = new Intent(this,AddCzatActivity.class);
        startActivity(intent);
    }

    // // // // // // // ZATWIERDZENIE WYBRANEGO CZATU
    private void button_on_click(){
        User user = new User(et_user_name.getText().toString());
        Intent intent = new Intent(this,CzatActivity.class);
        String message = et_user_name.getText().toString();
        intent.putExtra("message", message);
        startActivity(intent);
    }
    // ** ** ** ** ** ** ** ** ** ** ** **

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private void getMacAdress(){
        // POBRANIE ADRESU MAC URZĄDZENIA
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        macAddress = info.getMacAddress();
    }

    private void buildRetrofit(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        webService = retrofit.create(WebService.class);
    }

    private void getAnonymousUserName(){
        buildRetrofit();
        getMacAdress();

        try {
            final Call<UserAnonymous> userAnonymous = webService.userAnonymous(new AnonymousSend(macAddress));
            Log.d("MainActivity", "TRY");
            userAnonymous.enqueue(new Callback<UserAnonymous>() {
                @Override
                public void onResponse(Call<UserAnonymous> call, Response<UserAnonymous> response) {
                    Log.d("MainActivity", "ON RESPONSE");
                    Log.d("MainActivityResponse", response.toString());
                    userAnonymous1 = response.body();
                    App.getInstance().setUserAnonymous(userAnonymous1);
                    et_user_name.setText(userAnonymous1.name);
                    Log.d("UserAnonymous name", userAnonymous1.name);
                    Log.d("UserAnonymous ntoken", userAnonymous1.token);
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

}
