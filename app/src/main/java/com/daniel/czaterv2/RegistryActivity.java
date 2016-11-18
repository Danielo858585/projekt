package com.daniel.czaterv2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistryActivity extends Activity {


    TextView tv_registryUserName;
    TextView tv_registryUserEmail;
    TextView tv_registryUserPass;
    EditText et_registryUserName;
    EditText et_registryUserEmail;
    EditText et_registryUserPass;
    Button btn_registryAccept;

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

        btn_registryAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
