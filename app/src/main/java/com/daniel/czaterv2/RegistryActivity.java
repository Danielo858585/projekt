package com.daniel.czaterv2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistryActivity extends Activity {

    UserRegistry user;
    TextView tv_registryUserName;
    TextView tv_registryUserEmail;
    TextView tv_registryUserPass;
    EditText et_registryUserName;
    EditText et_registryUserEmail;
    EditText et_registryUserPass;
    Button btn_registryAccept;
    WebService webService;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        tv_registryUserName = (TextView) findViewById(R.id.tv_registryUserName);
        tv_registryUserEmail = (TextView) findViewById(R.id.tv_registryUserEmail);
        tv_registryUserPass = (TextView) findViewById(R.id.tv_registryUserPass);
        et_registryUserName = (EditText) findViewById(R.id.et_registryUserName);
        et_registryUserEmail = (EditText) findViewById(R.id.et_registryUserEmail);
        et_registryUserPass = (EditText) findViewById(R.id.et_registryUserPass);
        btn_registryAccept = (Button) findViewById(R.id.btn_registryRegistry);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.2:8080/puszek-1.0.0-SNAPSHOT/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        webService = retrofit.create(WebService.class);

        btn_registryAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameString = et_registryUserName.getText().toString();
                String userNamePass = et_registryUserPass.getText().toString();
                String userNameEmail = et_registryUserEmail.getText().toString();
                user = new UserRegistry();

                try{
                    Call<UserRegistry> createUser = webService.createUser(user);
                    Log.d("MainActivity","TRY");
                    createUser.enqueue(new Callback<UserRegistry>() {
                        @Override
                        public void onResponse(Call<UserRegistry> call, Response<UserRegistry> response) {
                            Log.d("MainActivity","ON RESPONSE");
                        }

                        @Override
                        public void onFailure(Call<UserRegistry> call, Throwable t) {
                            Log.d("MainActivity","ON FAILURE");
                        }
                    });
                }
                catch (Exception e){
                    Log.d("MainActivity", e.toString());
                }
            }
        });
    }


}
