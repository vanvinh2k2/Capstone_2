package com.example.abrrapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.models.User;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChangeProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView userimg;
    ImageButton cameraimg;
    TextView usernametxt;
    EditText fullnameedt, phoneedt, addressedt;
    Button updatebtn;
    int CodeCamera = 123;
    Bitmap anh = null;
    ReferenceManager manager;
    CompositeDisposable disposable = new CompositeDisposable();
    APIRestaurant apiRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        init();
        getToolBar();
        getData();
        process();
    }

    private void process() {
        cameraimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ChangeProfileActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CodeCamera);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CodeCamera);
                }
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    if(anh == null){
                        disposable.add(apiRestaurant.updateProfileNotImage(
                                        manager.getString("_id"),
                                        fullnameedt.getText().toString().trim(),
                                        phoneedt.getText().toString().trim(),
                                        addressedt.getText().toString().trim(),
                                        "Bearer "+manager.getString("access")
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        defaultModel -> {
                                            if(defaultModel.isSuccess()){
                                                finish();
                                            }
                                            Toast.makeText(ChangeProfileActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        },
                                        throwable -> {
                                            Toast.makeText(ChangeProfileActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }else{
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        anh.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
                        disposable.add(apiRestaurant.updateProfile(
                                        manager.getString("_id"),
                                        body,
                                        RequestBody.create(MediaType.parse("text/plain"), fullnameedt.getText().toString().trim()),
                                        RequestBody.create(MediaType.parse("text/plain"), phoneedt.getText().toString().trim()),
                                        RequestBody.create(MediaType.parse("text/plain"), addressedt.getText().toString().trim()),
                                        "Bearer "+manager.getString("access")
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        defaultModel -> {
                                            if(defaultModel.isSuccess()){
                                                finish();
                                            }
                                            Toast.makeText(ChangeProfileActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        },
                                        throwable -> {
                                            Toast.makeText(ChangeProfileActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CodeCamera){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CodeCamera);
            }
            else Toast.makeText(ChangeProfileActivity.this, "Ban ko cho phép mở Camera", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CodeCamera) {
                if (data != null && data.getExtras() != null) {
                    anh = (Bitmap) data.getExtras().get("data");
                    userimg.setImageBitmap(anh);
                }
            }
        }
    }

    private void getData() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("profile");
        Picasso.get().load(user.getAvatar()).placeholder(R.drawable.user).into(userimg);
        usernametxt.setText(user.getUsername());
        fullnameedt.setText(user.getFull_name());
        phoneedt.setText(user.getPhone());
        addressedt.setText(user.getAddress());
    }

    public boolean checkInput(){
        if(fullnameedt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field full name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phoneedt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(addressedt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field address!", Toast.LENGTH_SHORT).show();
            return false;
        }return true;
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

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        userimg = findViewById(R.id.profile_image);
        cameraimg = findViewById(R.id.camera);
        usernametxt = findViewById(R.id.username);
        fullnameedt = findViewById(R.id.fullname);
        phoneedt = findViewById(R.id.phone);
        addressedt = findViewById(R.id.address);
        updatebtn = findViewById(R.id.update);
        manager = new ReferenceManager(ChangeProfileActivity.this);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}