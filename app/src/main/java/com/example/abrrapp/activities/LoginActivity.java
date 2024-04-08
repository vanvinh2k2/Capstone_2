package com.example.abrrapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.models.User;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    TextView register, forgetpasswordtxt, googletxt;
    EditText emailedt, passwordedt;
    Button loginbtn;
    ImageView eyeimg;
    CheckBox show;
    LoginButton loginButton;
    APIRestaurant apiRestaurant;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CompositeDisposable disposable = new CompositeDisposable();
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        process();
        google();
    }

    private void google() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        googletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /*private void registerGoogle(String name1, String email1, String id1, String avatar1) {
        disposable.add(apiRestaurant.loginGoogle(avatar1, name1, email1, id1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Toast.makeText(this, "Bạn đã đăng nhập với Google", Toast.LENGTH_SHORT).show();
                                User user = (User) userModel.getData();
                                manager.putString("_id", user.get_id());
                                manager.putString("token", userModel.getAccessToken());
                                manager.putString("username", user.getDisplayName());
                                manager.putString("email", user.getEmail());
                                manager.putString("avatar", user.getAvatar());
                                manager.putString("isblock", user.getIsBlocked());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        throwable -> {
                            text(throwable.getMessage());
                        }
                ));
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Const.CODE_GOOGLE){
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct!=null){
                    String person = acct.getDisplayName();
                    String email = acct.getEmail();
                    String id = acct.getId();
                    String photoUrl = String.valueOf(acct.getPhotoUrl());
                    Log.e("kq_google","Name: "+person+"\tEmail: "+email+"\tIdGoogle: "+id+"\t Avatar: "+photoUrl);
                    //registerGoogle(person, email, id, photoUrl);
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }else{
            //callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void signIn() {
        Intent signIntent = gsc.getSignInIntent();
        startActivityForResult(signIntent, Const.CODE_GOOGLE);
    }

    private void process() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAccount();
            }
        });

        forgetpasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                /*String email = manager.getString("email").length()>0?manager.getString("email"):"";
                intent.putExtra("email", email);*/
                startActivity(intent);
            }
        });

        eyeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.isChecked()) {
                    show.setChecked(false);
                    eyeimg.setImageResource(R.drawable.eye);
                    passwordedt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    show.setChecked(true);
                    eyeimg.setImageResource(R.drawable.hidden);
                    passwordedt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        if(manager.getString("username")!=null) checkRefresh(manager.getString("refresh"));
    }

    private void checkRefresh(String refresh){
        disposable.add(apiRestaurant.checkToken(refresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        checkModel -> {
                            if(checkModel != null){
                                checkAccess(manager.getString("access"));
                            }
                            else{
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+" 1", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void checkAccess(String access){
        disposable.add(apiRestaurant.checkToken(access)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        checkModel -> {
                            if(checkModel != null){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                refreshToken(manager.getString("refresh"));
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+" 2", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void refreshToken(String refresh){
        disposable.add(apiRestaurant.refreshToken(refresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> {
                            manager.putString("access", token.getAccess());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+" 3", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void loginAccount() {
        String email1 = emailedt.getText().toString().trim();
        String password1 = passwordedt.getText().toString().trim();
        if(checkInput()){
            checkAccount(email1, password1);
        }
    }

    private void checkAccount(String email1, String password1) {
        disposable.add(apiRestaurant.login(email1, password1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                User user = (User) userModel.getData();
                                manager.putString("_id", user.getId());
                                manager.putString("refresh", user.getToken().getRefresh());
                                manager.putString("access", user.getToken().getAccess());
                                manager.putString("username", user.getUsername());
                                try {
                                    manager.putString("email", user.getEmail());
                                }catch(Exception e){
                                    Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                                manager.putString("avatar", user.getAvatar());
                                manager.putString("is_active", user.getIs_active()+"");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                text("Email or Password is wrong!");
                            }
                        },
                        throwable -> {
                            text(throwable.getMessage());
                            Log.e("er",throwable.getMessage());
                        }
                ));
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
        eyeimg = findViewById(R.id.eye);
        //loginButton = findViewById(R.id.login_button);
        manager = new ReferenceManager(getApplicationContext());
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}