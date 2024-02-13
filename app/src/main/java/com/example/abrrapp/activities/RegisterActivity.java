package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    TextView logintxt;
    EditText nameedt, emailedt, passwordedt, copasswordedt;
    Button createbtn;
    ImageView eye1, eye2;
    CheckBox show1, show2;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        process();
    }
    boolean checkInput(){
        if(nameedt.getText().toString().trim().isEmpty()){
            text("Please input name!");
            nameedt.requestFocus();
            return false;
        }else if(emailedt.getText().toString().trim().isEmpty()){
            text("Please input email!");
            emailedt.requestFocus();
            return false;
        }else if(passwordedt.getText().toString().trim().isEmpty()){
            text("Please input password!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().trim().isEmpty()){
            text("Please input re-password!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().compareTo(passwordedt.getText().toString())!=0){
            text("Password and re-password is not same!");
            copasswordedt.requestFocus();
            return false;
        }else return true;
    }

    private void process() {
        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        String name1 = nameedt.getText().toString().trim();
        String email1 = emailedt.getText().toString().trim();
        String password1 = passwordedt.getText().toString().trim();
        if(checkInput()){
            register(name1, email1, password1);
        }
    }

    private void register(String name1, String email1, String password1) {
        disposable.add(apiRestaurant.register(name1, email1, password1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                text("Tạo tài khoản thành công");
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                text("Tài khoản đã tồn tại!");
                            }
                        },
                        throwable -> {
                            text(throwable.getMessage());
                        }
                ));
    }

    void text(String v){
        Toast.makeText(this, v+"", Toast.LENGTH_SHORT).show();
    }
    public void init(){
        logintxt = findViewById(R.id.logintxt);
        nameedt = findViewById(R.id.name);
        emailedt = findViewById(R.id.email);
        passwordedt = findViewById(R.id.password);
        copasswordedt = findViewById(R.id.passwordcf);
        createbtn = findViewById(R.id.create);
        eye1 = findViewById(R.id.eyeimg);
        eye2 = findViewById(R.id.eyeimg2);
        show1 = findViewById(R.id.show1);
        show2 = findViewById(R.id.show2);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(getApplicationContext());
    }
}