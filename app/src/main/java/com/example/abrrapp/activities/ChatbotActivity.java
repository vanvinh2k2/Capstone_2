package com.example.abrrapp.activities;

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
import com.example.abrrapp.adapter.ChatbotAdapter;
import com.example.abrrapp.models.Chatbot;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitChatbot;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatbotActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView messagercv;
    EditText contenttxt;
    FrameLayout sendfl;
    ReferenceManager manager;
    APIRestaurant apiRestaurant;
    ChatbotAdapter adapter;
    List<Chatbot> chatbotList;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        init();
        getToolbar();
        getListChat();
        sendRequest();
    }

    private void getListChat() {
        disposable.add(apiRestaurant.listChatbot("102566218799174938142")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        chatbotModel -> {
                            if(chatbotModel.isSuccess()){
                                chatbotList.clear();
                                chatbotList.add(new Chatbot("Hi, can I help you?", "chatbodyinit"));
                                chatbotList.addAll(chatbotModel.getData());
                                adapter = new ChatbotAdapter(ChatbotActivity.this, R.layout.item_chatbot, chatbotList);
                                messagercv.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void sendRequest(){
        sendfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = contenttxt.getText().toString().trim();
                chatbotList.add(new Chatbot("...", body));
                adapter.notifyDataSetChanged();
                messagercv.smoothScrollToPosition(chatbotList.size()-1);
                disposable.add(apiRestaurant.sendChatbot("102566218799174938142", body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatbotModel -> {
                                    if(chatbotModel.isSuccess()){
                                        contenttxt.setText("");
                                        getListChat();
                                        messagercv.smoothScrollToPosition(chatbotList.size()-1);
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(ChatbotActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        });
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
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        contenttxt = findViewById(R.id.inputMessage);
        sendfl = findViewById(R.id.layoutSend);
        manager = new ReferenceManager(ChatbotActivity.this);
        messagercv = findViewById(R.id.listMessage);
        chatbotList = new ArrayList<>();
        apiRestaurant = RetrofitChatbot.getInstance(Const.CHATBOT_URL).create(APIRestaurant.class);
    }
}