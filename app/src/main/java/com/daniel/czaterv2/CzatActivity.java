package com.daniel.czaterv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.daniel.czaterv2.model.ChatMessage;
import com.daniel.czaterv2.model.UserType;
import com.daniel.czaterv2.widgets.EmojiView;
import com.daniel.czaterv2.widgets.SizeNotifierRelativeLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.internal.zzh;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.client.StompClient;

public class CzatActivity extends AppCompatActivity implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate {

    private static final String TAG = "CzatActivity";
    private ChatAdapter listAdapter;
    private EmojiView emojiView;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean showingEmoji;
    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;
    private ListView chatListView;      //Lista wiadomości
    private EditText chatEditText1;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView enterChatView1, emojiButton;
    private StompClient mStompClient;
    private ObjectMapper objectMapper = new ObjectMapper();
    private WebService webService;
    private Intent intent;
    MqttAndroidClient client;
    String id;
    String topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_czat);


        chatMessages = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) findViewById(R.id.enter_chat1);
        listAdapter = new ChatAdapter(chatMessages, this);
        chatListView.setAdapter(listAdapter);
        chatEditText1.setOnKeyListener(keyListener);
        enterChatView1.setOnClickListener(clickListener);
        chatEditText1.addTextChangedListener(watcher1);
        sizeNotifierRelativeLayout = (SizeNotifierRelativeLayout) findViewById(R.id.chat_layout);
        sizeNotifierRelativeLayout.delegate = this;
        intent = getIntent();
        id = intent.getStringExtra("id");
        topic = "chatRequest/" + id;
        buildRetrofit();

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://138.68.77.240:1883", clientId);
        try {
            IMqttToken token = client.connect();
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, new String(message.getPayload(), "UTF-8"));
                    MessageResponse response = objectMapper.readValue(message.getPayload(), MessageResponse.class);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessageText(response.getTextMessage());
                    if (response.getAuthor().equals(App.getInstance().getUser().getName())) {
                        chatMessage.setUserType(UserType.SELF);
                    } else {
                        chatMessage.setUserType(UserType.OTHER);
                    }
                    chatMessage.setAuthor(response.getAuthor());
                    chatMessage.setMessageTime(response.getTime());
                    chatMessages.add(chatMessage);

                    CzatActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess - MQTT");
                    int qos = 1;
                    topic = "chatResponse/" + id;
                    try {
                        client.subscribe(topic, qos);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure - MQTT");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

//        NotificationCenter.getInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
//        Call<ChatDetailsResponse> call = webService.getChatDetails(new ChatDetailsRequest(id));
//        call.enqueue(new Callback<ChatDetailsResponse>() {
//            @Override
//            public void onResponse(Call<ChatDetailsResponse> call, Response<ChatDetailsResponse> response) {
//                Log.d("CzatActivity", "onResponse - getChatDetails");
//                try {
//                    for (MessageResponse messageResponse : response.body().getMessagesList()) {
//                        final ChatMessage message = new ChatMessage();
//                        message.setMessageStatus(Status.SENT);
//                        message.setAuthor(messageResponse.getAuthor());
//                        message.setMessageText(messageResponse.getTextMessage());
//                        message.setMessageTime(message.getMessageTime());
//                        chatMessages.add(message);
//                    }
//                } catch (Exception e) {
//                    Log.d("Exception", e.toString());
//                }
//
//                listAdapter.notifyDataSetChanged();
//                scrollMyListViewToBottom();
//            }
//
//            @Override
//            public void onFailure(Call<ChatDetailsResponse> call, Throwable t) {
//                Log.d("CzatActivity", "onFailure - getChatDetails");
//            }
//        });
    }

    private void sendMessage(final String messageText, final UserType userType) {
        String topic = "chatRequest/" + id;
        JSONObject messageJson = createMessage(messageText);
        String payload = messageJson.toString();
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createMessage(String messageText) {
        JSONObject message = new JSONObject();
        try {
            message.put("author", App.getInstance().getUser().getName());
            message.put("textMessage", messageText);
            message.put("tokenContent", App.getInstance().getUser().getToken());
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void scrollMyListViewToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatListView.setSelection(listAdapter.getCount() - 1);
            }
        });
    }

    private Activity getActivity() {
        return this;
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.emojiDidLoaded) {
            if (emojiView != null) {
                emojiView.invalidateViews();
            }

            if (chatListView != null) {
                chatListView.invalidateViews();
            }
        }
    }

    @Override
    public void onSizeChanged(int height) {

        Rect localRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null) {
            return;
        }


        if (height > AndroidUtilities.dp(50) && keyboardVisible) {
            keyboardHeight = height;
            App.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }


        if (showingEmoji) {
            int newHeight = 0;

            newHeight = keyboardHeight;

            if (windowLayoutParams.width != AndroidUtilities.displaySize.x || windowLayoutParams.height != newHeight) {
                windowLayoutParams.width = AndroidUtilities.displaySize.x;
                windowLayoutParams.height = newHeight;

                wm.updateViewLayout(emojiView, windowLayoutParams);
                if (!keyboardVisible) {
                    sizeNotifierRelativeLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sizeNotifierRelativeLayout != null) {
                                sizeNotifierRelativeLayout.setPadding(0, 0, 0, windowLayoutParams.height);
                                sizeNotifierRelativeLayout.requestLayout();
                            }
                        }
                    });
                }
            }
        }


        boolean oldValue = keyboardVisible;
        keyboardVisible = height > 0;
//        if (keyboardVisible && sizeNotifierRelativeLayout.getPaddingBottom() > 0) {
//            showEmojiPopup(false);
//        } else if (!keyboardVisible && keyboardVisible != oldValue && showingEmoji) {
//            showEmojiPopup(false);
//        }
    }

    private void buildRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.getSendURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        webService = retrofit.create(WebService.class);
    }


    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if (v == chatEditText1) {
                    sendMessage(editText.getText().toString(), UserType.SELF);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;
        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == enterChatView1) {
                sendMessage(chatEditText1.getText().toString(), UserType.SELF);
            }
            chatEditText1.setText("");
        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {

            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Czy opuścić czat?")
                .setMessage("Czy napewno chcesz opuścić czat? Rozmowa nie zostanie zachowana!")
                .setPositiveButton("TAK", new zzh() {
                    @Override
                    protected void zzavx() {
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                }).setNeutralButton("NIE", new zzh() {
            @Override
            protected void zzavx() {

            }
        });

        builder.create();
    }
}

//        mStompClient = Stomp.over(WebSocket.class, "ws://138.68.77.240:8080/puszek/chat/websocket");
//        mStompClient.connect();
//        mStompClient.topic("/topic/messages").subscribe(new Action1<StompMessage>() {
//            @Override
//            public void call(StompMessage topicMessage) {
//                Log.d("Stom Call", topicMessage.toString());
//                final ChatMessage message = new ChatMessage();
//
//
//                message.setMessageStatus(Status.SENT);
//
//                try {
//                    MessageResponse response = objectMapper.readValue(topicMessage.getPayload(), MessageResponse.class);
//                    message.setMessageText(response.getTextMessage());
//
//
//                    Log.d("Czat Activity", "Try");
//                    if (response.getAuthor().equals(App.getInstance().getUser().getLogin())) {
//                        message.setUserType(UserType.SELF);
//                    } else {
//                        message.setUserType(UserType.OTHER);
//                    }
//                    message.setAuthor(response.getAuthor());
//                    message.setMessageTime(response.getTime());
//                    chatMessages.add(message);
//
//                    CzatActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            listAdapter.notifyDataSetChanged();
//                            scrollMyListViewToBottom();
//                            Log.d("Czat Activity", "runOnUiThread");
//                        }
//                    });
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });