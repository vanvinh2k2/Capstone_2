package com.example.abrrapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactUsFragment extends Fragment {
    TextView titletxt, contenttxt;
    Button sendbtn;
    CompositeDisposable disposable = new CompositeDisposable();
    APIRestaurant apiRestaurant;
    ReferenceManager manager;
    FloatingActionButton chatbot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        init(view);
        process();
        return view;
    }

    boolean checkInput(){
        if(titletxt.getText().toString().trim().isEmpty()){
            text("Please input title!");
            titletxt.requestFocus();
            return false;
        }else if(contenttxt.getText().toString().trim().isEmpty()){
            text("Please input content!");
            contenttxt.requestFocus();
            return false;
        }else return true;
    }

    void text(String v){
        Toast.makeText(getContext(), v+"", Toast.LENGTH_SHORT).show();
    }

    private void process() {
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    disposable.add(apiRestaurant.contactUs(
                                    manager.getString("username"),
                                    manager.getString("email"),
                                    manager.getString("phone"),
                                    titletxt.getText().toString().trim(),
                                    contenttxt.getText().toString().trim()
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    defaultModel -> {
                                        if(defaultModel.isSuccess()){
                                            Toast.makeText(getContext(), defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatbotActivity.class));
            }
        });
    }

    public void init(View view){
        titletxt = view.findViewById(R.id.title);
        contenttxt = view.findViewById(R.id.content);
        sendbtn = view.findViewById(R.id.send);
        chatbot = view.findViewById(R.id.chatbot);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(getActivity());
    }
}