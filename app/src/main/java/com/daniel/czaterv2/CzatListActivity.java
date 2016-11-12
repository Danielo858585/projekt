package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.security.SecureRandom;

public class CzatListActivity extends Activity {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    private TextView tv_user_name;
    private EditText et_user_name;
    private ListView czat_list;
    private Button czat_list_accept;
    private Button sprawdz;
    private Button addCzat;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czat_list);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        czat_list = (ListView) findViewById(R.id.lv_czat_list);
        czat_list_accept = (Button) findViewById(R.id.btn_accept_czat_list);
        sprawdz = (Button) findViewById(R.id.btn_sprawdz);
        addCzat = (Button) findViewById(R.id.btn_addCzat);

        czat_list_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_on_click();
            }
        });
        sprawdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sprawdz();
            }
        });
        addCzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCzatOnClick();
            }
        });

        if(MySingleton.isNull()){
            et_user_name.setText(randomString(10));         // ustawienie przy pomocy funkcji randomString losowej nazwy uzytkownika
        }
        else{
            et_user_name.setText(MySingleton.getInstance().getUser().getName());
        }


    }

    private void addCzatOnClick() {
        Intent intent = new Intent(this,AddCzatActivity.class);
        startActivity(intent);
    }

    private void button_on_click(){

        User user = new User(et_user_name.getText().toString());
        Intent intent = new Intent(this,CzatActivity.class);
        String message = et_user_name.getText().toString();
        intent.putExtra("message", message);
        startActivity(intent);
    }
    private void sprawdz(){
        String message = et_user_name.getText().toString();
        Log.d(message,"a");
    }

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
