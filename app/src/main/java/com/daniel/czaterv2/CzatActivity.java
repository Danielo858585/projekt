package com.daniel.czaterv2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CzatActivity extends Activity {

    private TextView user_name;
    private String intent_message;
    private ListView czat;
    private ArrayList<String> example;
    private EditText message;
    private Button send_message;
    private ArrayAdapter<String> adapter;

    private static final String CLASS_TAG = "CZAT_ACTIVITY";
    private Retrofit retrofit;
    WebService webService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czat);
        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder().baseUrl("http://localhost:8180").addConverterFactory(GsonConverterFactory.create()).build();
        webService = retrofit.create(WebService.class);
        user_name = (TextView) findViewById(R.id.czat_user_name);
        czat = (ListView) findViewById(R.id.lv_czat_show);
        message = (EditText) findViewById(R.id.et_message);
        example = new ArrayList<String>();
        send_message = (Button) findViewById(R.id.btn_czat_send);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_simple_list_view);

        initResources();
        initCzat();

        Intent i = getIntent();
        intent_message = i.getStringExtra("message");
        user_name.setText(intent_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MessageBody body = new MessageBody();
                    body.setNameUser(user_name.toString());
                    body.setMessage(message.toString());
                    body.setId(1);
                    Log.d(CLASS_TAG,"Jestem w submit");
//                    Call<MessageBody> call = webService.loadRepo();
//                    call.enqueue(new Callback<MessageBody>() {
//                        @Override
//                        public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {
//                            Log.d(CLASS_TAG, response.toString());
//                        }
//
//                        @Override
//                        public void onFailure(Call<MessageBody> call, Throwable t) {
//                            Log.e(CLASS_TAG + " Throwable", t.toString());
//                        }
//                    });
                }
                catch (Exception e){
                    Log.e(CLASS_TAG + " Exception",e.toString());
                }
            }
        });


        /*
        ------------- WERSJA TESTOWA LOKALNA ---------------------
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(message.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        ------------- KONIEC WERSJI TESTOWEJ ---------------------
        */

    }

    private void initResources(){
        Resources res = getResources();
    }

    private void initCzat(){
        czat.setAdapter(adapter);
    }


   /*
   ----------------- ON CLICK Z BUTTER KNIFA --------------------
    @OnClick(R.id.btn_czat_send)

    public void submit(){
        try {
            MessageBody body = new MessageBody();
            body.setNameUser(user_name.toString());
            body.setMessage(message.toString());
            body.setId(1);
            Log.d(CLASS_TAG,"Jestem w submit");
            webService.postData(body, new Callback<MessageBody>() {
                @Override
                public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {
                    Log.d(CLASS_TAG, response.toString());
                }

                @Override
                public void onFailure(Call<MessageBody> call, Throwable t) {
                    Log.e(CLASS_TAG, t.toString());
                }
            });
        }
        catch (Exception e){
            Log.e(CLASS_TAG,e.toString());
        }
    }
    ----------------- KONIEC ON CLICK Z BUTTER KNIFA --------------------
    */



    /*
    ------------------  DO USUNIĘCIA  -------------------
    @OnClick (R.id.btn_czat_send)
    public void submit(View view){
        try{
            webService.getData(new Callback<MessageBody>() {
                @Override
                public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {
                    Log.d(CLASS_TAG, call.toString());
                }

                @Override
                public void onFailure(Call<MessageBody> call, Throwable t) {
                    Log.d(CLASS_TAG, t.getLocalizedMessage());
                }
            });
        }
        catch (Exception e){
            Log.d(CLASS_TAG, e.toString());
        }

    }
    ------------------  DO USUNIĘCIA KONIEC  -------------------
    */
}
