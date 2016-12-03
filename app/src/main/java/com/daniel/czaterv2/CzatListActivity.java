package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.security.SecureRandom;
import java.util.ArrayList;

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
    private CzatProperties czat1;
    private CzatProperties czat2;
    private CzatProperties czat3;


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
        czat1 = new CzatProperties();
        czat1.setName("Czat 1");
        czat1.setRange(5000);
        czat1.setPosition(new LatLng(22.000,56.000));
        czat1.setMaxUsers(10);

        czat2 = new CzatProperties();
        czat2.setName("Czat 2");
        czat2.setRange(10000);
        czat2.setPosition(new LatLng(22.100,56.200));
        czat2.setMaxUsers(10);

        czat3 = new CzatProperties();
        czat3.setName("Czat 3");
        czat3.setRange(15000);
        czat3.setPosition(new LatLng(22.200,56.100));
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

        if(App.getInstance().getUser()==null){
            et_user_name.setText(randomString(10));         // ustawienie przy pomocy funkcji randomString losowej nazwy uzytkownika
        }
        else{
            et_user_name.setText(App.getInstance().getUser().getLogin());
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
