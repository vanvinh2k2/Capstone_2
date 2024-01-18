package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;

public class RegisterActivity extends AppCompatActivity {
    TextView logintxt;
    EditText nameedt, emailedt, passwordedt, copasswordedt;
    Button createbtn;
    ImageView eye1, eye2;
    CheckBox show1, show2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
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
    }
}