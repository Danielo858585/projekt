package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private EditText login;
    private EditText pass;
    private Button acceptLogin;
    private Button registry;
    private boolean dataComplete;
    private User user;
    private String nameString;
    private String passString;
    private Intent intentParent;
    private Bundle bundle;
    final static int LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.et_login);
        pass = (EditText) findViewById(R.id.et_password);
        acceptLogin = (Button) findViewById(R.id.btn_acceptLogin);
        registry = (Button) findViewById(R.id.btn_goToRegistry);
        intentParent = getIntent();

        acceptLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdata();
                if (dataComplete == true) {

                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.43.213:8180/")
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .client(client)
                            .build();

//                    WebService webService = retrofit.create(WebService.class);
//                    Call<Void> call = webService.login(new User("viader", 1, "password", "viader@gmail.com"));
//                    call.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//
//                        }
//                    });

                    nameString = login.getText().toString();
                    passString = pass.getText().toString();
                    user = new User(nameString, passString);
                    App.getInstance().setUser(user);
                    setResult(RESULT_OK, intentParent);
                    finish();
                    Log.d("A","Jestem w IFIE");
                } else {
                    login.setText("");
                    pass.setText("");
                    Log.d("A","Jestem w ELSIE");
                }
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentParent = new Intent(getApplicationContext(), RegistryActivity.class);
                startActivity(intentParent);
            }
        });
    }

    private void checkdata() {
        dataComplete = false;
        if (login.getText().toString().length() <= 0) {
            Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz login", Toast.LENGTH_SHORT);
            tost.show();
        } else if (pass.getText().toString().length() <= 0) {
            Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz hasło", Toast.LENGTH_SHORT);
            tost.show();
        } else {
            dataComplete = true;
        }
        Log.d("dataComplete", String.valueOf(dataComplete));
    }
}
