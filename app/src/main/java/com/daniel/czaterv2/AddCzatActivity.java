package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCzatActivity extends Activity {

    private EditText czatName;
    private EditText maxUsers;
    private Button defineCzatLocation;

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
                Integer maxUsersInt = Integer.parseInt(maxUsers.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("czatName",czatNameString);
                bundle.putInt("maxUsers",maxUsersInt);
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void defineCzatCenter() {

    }
}
