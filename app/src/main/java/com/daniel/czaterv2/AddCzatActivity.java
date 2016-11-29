package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class AddCzatActivity extends Activity {

    private EditText czatName;
    private TextView maxUsersView;
    private TextView rangeView;
    private Button defineCzatLocation;
    private CzatProperties czatProperties;
    private JSONObject jsonObject;
    private Boolean dataComplete;
    private SeekBar maxUsers;
    private SeekBar czatRange;
    int maxUsersInt;
    int czatRangeInt;
    private static final int GET_CZAT_CENTER_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_czat);

        czatName = (EditText) findViewById(R.id.et_czatName);
        maxUsers = (SeekBar) findViewById(R.id.sb_maxUsers);
        czatRange = (SeekBar) findViewById(R.id.sb_range);
        defineCzatLocation = (Button) findViewById(R.id.btn_defineCzatCenter);
        maxUsersView = (TextView) findViewById(R.id.tv_sbMaxUsersView);
        rangeView = (TextView) findViewById(R.id.tv_sbRangeView);
        jsonObject = new JSONObject();
        maxUsersInt = 2;
        czatRangeInt = 1000;
        maxUsers.setLeft(1);
        maxUsers.setRight(10);

        czatRange.setLeft(1);
        czatRange.setRight(10000);

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


        defineCzatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String czatNameString = czatName.getText().toString();

                if (czatNameString.length() <= 0) {
                    Toast tost = Toast.makeText(getApplicationContext(), "Wprowadz nazwe czatu", Toast.LENGTH_SHORT);
                    tost.show();
                } else {
                    //czatProperties.setMaxUsers(maxUsers2);
//                    czatProperties.setName(czatName.getText().toString());
   //                 czatProperties.setRange(Integer.parseInt(czatRange.getText().toString()));
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("range",czatRangeInt);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, GET_CZAT_CENTER_INTENT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {


    }

    private void defineCzatCenter() {

    }
}
