package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    ReferenceManager manager;
    Button confirmbtn;
    EditText passwordedt, copasswordedt, currentPasswordedt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        getToolBar();
        changePassword();
    }

    boolean checkInput(){
        if(currentPasswordedt.getText().toString().trim().isEmpty()){
            text("Please input current password!");
            passwordedt.requestFocus();
            return false;
        }else if(passwordedt.getText().toString().trim().isEmpty()){
            text("Please input new password!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().trim().isEmpty()){
            text("Please input new re-password!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().compareTo(passwordedt.getText().toString())!=0){
            text("Password and re-password is not same!");
            copasswordedt.requestFocus();
            return false;
        }else return true;
    }

    private void changePassword() {
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    disposable.add(apiRestaurant.updatePassword(
                                    manager.getString("_id"),
                                    currentPasswordedt.getText().toString().trim(),
                                    passwordedt.getText().toString().trim(),
                                    "Bearer "+manager.getString("access")
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    defaultModel -> {
                                        text(defaultModel.getMessage());
                                        if(defaultModel.isSuccess()) finish();
                                    },
                                    throwable -> {text(throwable.getMessage());}
                            ));
                }
            }
        });
    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
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

    void text(String v){
        Toast.makeText(ChangePasswordActivity.this, v+"", Toast.LENGTH_SHORT).show();
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        manager = new ReferenceManager(this);
        passwordedt = findViewById(R.id.newpass);
        copasswordedt = findViewById(R.id.confirmpass);
        currentPasswordedt = findViewById(R.id.currentpass);
        confirmbtn = findViewById(R.id.confirm);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}