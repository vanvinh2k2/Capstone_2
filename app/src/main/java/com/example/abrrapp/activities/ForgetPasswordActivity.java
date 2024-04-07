package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextView emailedt;
    private Button sendbtn;
    CompositeDisposable disposable = new CompositeDisposable();
    APIRestaurant apiRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
        sendEmail();
    }

    private void sendEmail() {
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailedt.getText().toString().trim();
                if(email.length() > 0){
                    disposable.add(apiRestaurant.forgetPassword(email).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    defaultModel -> {
                                        if(defaultModel.isSuccess()){
                                            Toast.makeText(ForgetPasswordActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }else  Toast.makeText(ForgetPasswordActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    },
                                    throwable -> {
                                        Toast.makeText(ForgetPasswordActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "Please input Email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(){
        emailedt = findViewById(R.id.email);
        sendbtn = findViewById(R.id.confirm);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}