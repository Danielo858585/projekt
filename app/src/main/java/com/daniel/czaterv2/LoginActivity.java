package com.daniel.czaterv2;

import android.app.Activity;
import android.app.AlertDialog;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private EditText login;
    private EditText pass;
    private Button acceptLogin;
    private Button backFromLogin;
    private boolean dataComplete;
    private UserLoginResponse userLoginResponse;
    private String nameString;
    private String passString;
    private Intent intentParent;
    private Bundle bundle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.et_login);
        pass = (EditText) findViewById(R.id.et_password);
        acceptLogin = (Button) findViewById(R.id.btn_acceptLogin);
        backFromLogin = (Button) findViewById(R.id.btn_backFromLogin);
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
                            .baseUrl(App.getSendURL())
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .client(client)
                            .build();

                    WebService webService = retrofit.create(WebService.class);
                    Call<UserLoginResponse> call = webService.userLogin(new UserLoginRequest(login.getText().toString(), pass.getText().toString()));
                    call.enqueue(new Callback<UserLoginResponse>() {
                        @Override
                        public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                            if (response.code() == 200) {
                                userLoginResponse = response.body();
                                user = new User(userLoginResponse);
                                App.getInstance().setUser(user);
                                setResult(RESULT_OK, intentParent);
                                finish();
                            } else if (response.code() == 422) {
                                Toast tost = Toast.makeText(getApplicationContext(), "Brak użytkownika " + login.getText().toString(), Toast.LENGTH_LONG);
                                tost.show();
                            }
                            else{
                                Toast tost = Toast.makeText(getApplicationContext(), "Wystąpił nieoczekiwany błąd", Toast.LENGTH_LONG);
                                tost.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginResponse> call, Throwable t) {

                        }
                    });
                    Log.d("LoginActivity", "Jestem w IFIE");
                } else {
                    login.setText("");
                    pass.setText("");
                    Log.d("A", "Jestem w ELSIE");
                }
            }
        });

        backFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, intentParent);
                finish();
            }
        });
    }

    private void checkdata() {
        dataComplete = false;
        if (login.getText().toString().length() <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Wprowadz login!").setTitle("Brak loginu");
            AlertDialog dialog = builder.create();
            dialog.show();
//            Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz login", Toast.LENGTH_SHORT);
//            tost.show();
        } else if (pass.getText().toString().length() <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Wprowadz hasło!").setTitle("Brak hasła");
            AlertDialog dialog = builder.create();
            dialog.show();
//            Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz hasło", Toast.LENGTH_SHORT);
//            tost.show();
        } else {
            dataComplete = true;
        }
        Log.d("dataComplete", String.valueOf(dataComplete));
    }
}
