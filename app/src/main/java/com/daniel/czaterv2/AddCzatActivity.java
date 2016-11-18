package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCzatActivity extends Activity {

    private EditText czatName;
    private EditText maxUsers;
    private Button defineCzatLocation;
    private static final int GET_CZAT_CENTER_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_czat);

        czatName = (EditText) findViewById(R.id.et_czatName);
        maxUsers = (EditText) findViewById(R.id.et_maxUsers);
        defineCzatLocation = (Button) findViewById(R.id.btn_defineCzatCenter);

        defineCzatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String czatNameString = czatName.getText().toString();
                String maxUsersInt = maxUsers.getText().toString();
                if(czatNameString==""){
                    Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz nazwe czatu", Toast.LENGTH_SHORT);
                    tost.show();
                }
                else if (maxUsersInt==""){
                    Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz ilosc uytkownikow", Toast.LENGTH_SHORT);
                    tost.show();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("czatName",czatNameString);
                    bundle.putString("maxUsers",maxUsersInt);
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, GET_CZAT_CENTER_INTENT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent){


    }

    private void defineCzatCenter() {

    }
}
