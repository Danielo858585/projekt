package com.daniel.czaterv2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistryActivity extends Activity {
    @BindView(R.id.tv_registryUserName)TextView tv_registryUserName;
    @BindView(R.id.tv_registryUserEmail)TextView tv_registryUserEmail;
    @BindView(R.id.tv_registryUserPass)TextView tv_registryUserPass;
    @BindView(R.id.et_registryUserName)EditText et_registryUserName;
    @BindView(R.id.et_registryUserEmail)EditText et_registryUserEmail;
    @BindView(R.id.et_registryUserPass) EditText et_registryUserPass;
    @BindView(R.id.btn_registryRegistry) Button btn_registryAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_registryRegistry)
    public void registry (View view){

    }
}
