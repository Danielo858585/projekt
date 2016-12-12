package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button btn_backFromRegistry;
    WebService webService;
    Retrofit retrofit;
    Intent intentParent;
    //final static int REGISTRY = 2;

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
        btn_backFromRegistry = (Button) findViewById(R.id.btn_backFromRegistry);
        intentParent = getIntent();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(App.getSendURL())
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
                user = new UserRegistry(userNameEmail, userNameString, userNamePass);

                try {
                    Call<Void> createUser = webService.createUser(user);
                    Log.d("RegistryActivity", "TRY");
                    createUser.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("RegistryActivity", "ON RESPONSE");
                            if (response.code() == 200) {
                                Toast tost = Toast.makeText(getApplicationContext(), "Rejestracja została przeprowadzona prawidłowo",
                                        Toast.LENGTH_LONG);
                                tost.show();
                                setResult(RESULT_OK, intentParent);
                                finish();
                            } else if (response.code() == 422) {
                                Toast tost = Toast.makeText(getApplicationContext(),
                                        "Brak możliwości rejestracji. Skontaktuj się administracją",
                                        Toast.LENGTH_SHORT);
                                tost.show();
                                Log.d("RegistryActivity - 422", response.toString());
                            } else {
                                Log.d("RegistryActivity", "Wystąpił nieoczekiwany błąd");
                                Toast tost = Toast.makeText(getApplicationContext(), "Wystąpił nieoczekiwany błąd", Toast.LENGTH_LONG);
                                tost.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("RegistryActivity", "ON FAILURE");
                        }
                    });
                } catch (Exception e) {
                    Log.d("RegistryActivity", e.toString());
                }
            }
        });
        btn_backFromRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, intentParent);
                finish();
            }
        });
    }

}
