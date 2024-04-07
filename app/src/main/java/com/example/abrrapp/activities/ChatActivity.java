package com.example.abrrapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.MessageAdapter;
import com.example.abrrapp.models.Message;
import com.example.abrrapp.utils.Const;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView receiverimg;
    TextView receivertxt;
    RecyclerView messagercv;
    EditText contenttxt;
    FrameLayout sendfl;
    private WebSocket webSocket;
    MessageAdapter adapter;
    List<Message> messageList;
    String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        getToolbar();
        connectWebSocket();
        sendMessage();
    }

    private void sendMessage(){
        sendfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToServer(contenttxt.getText().toString().trim());
                contenttxt.setText("");
            }
        });
    }
    private void getListChat(){
        JSONObject source = new JSONObject();
        try {
            source.put("source", "message-list");
            source.put("friend", receiver);
            webSocket.send(source.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://"+ Const.URL +"/ws/chat/102566218799174938142/")
                .build();
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("ok: ", "loi");
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                Gson gson = new Gson();
                messagercv = findViewById(R.id.listMessage);
                JsonArray jsonArray = gson.fromJson(text, JsonArray.class);
                messageList = new ArrayList<>();
                for (int i=0;i<jsonArray.size(); i++){
                    JsonElement element = jsonArray.get(i);
                    String body = element.getAsJsonObject().get("body").getAsString();
                    String sender = element.getAsJsonObject().get("sender").getAsString();
                    String datetxt = element.getAsJsonObject().get("date").getAsString().substring(0, 20);
                    JsonObject msgUser = element.getAsJsonObject().get("msg_user").getAsJsonObject();
                    JsonObject msgRestaurant = element.getAsJsonObject().get("msg_restaurant").getAsJsonObject();
                    String idUser = msgUser.getAsJsonObject().get("id").getAsString();
                    String idRes = msgRestaurant.getAsJsonObject().get("rid").getAsString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = null;
                    try {
                        date = dateFormat.parse(datetxt);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    messageList.add(new Message(body, idUser, idRes, sender, date));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MessageAdapter(messageList, R.drawable.user);
                        messagercv.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                getListChat();
            }
        };
        webSocket = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }

    private void sendDataToServer(String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("source", "message-user");
            jsonObject.put("friend", receiver);
            jsonObject.put("message", data);
            webSocket.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        Intent intent = getIntent();
        receiver = intent.getStringExtra("usernameRes");
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        receiverimg = findViewById(R.id.receiver);
        receivertxt = findViewById(R.id.name);
        contenttxt = findViewById(R.id.inputMessage);
        sendfl = findViewById(R.id.layoutSend);
    }
}