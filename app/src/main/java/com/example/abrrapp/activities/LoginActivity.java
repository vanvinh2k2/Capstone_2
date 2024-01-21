package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    TextView register, forgetpasswordtxt, googletxt;
    EditText emailedt, passwordedt;
    Button loginbtn;
    ImageView eye;
    CheckBox show;
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        process();
    }

    private void process() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    boolean checkInput(){
        if(emailedt.getText().toString().trim().isEmpty()){
            text("Please input Email!");
            emailedt.requestFocus();
            return false;
        }else if(passwordedt.getText().toString().trim().isEmpty()){
            text("Please input Password!");
            passwordedt.requestFocus();
            return false;
        }else return true;
    }
    void text(String v){
        Toast.makeText(this, v+"", Toast.LENGTH_SHORT).show();
    }
    public void init(){
        register = findViewById(R.id.registertxt);
        forgetpasswordtxt = findViewById(R.id.forgetpassword);
        googletxt = findViewById(R.id.login_google);
        emailedt = findViewById(R.id.email);
        passwordedt = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginaccount);
        show = findViewById(R.id.show);
        eye = findViewById(R.id.eye);
        loginButton = findViewById(R.id.login_button);
    }
}