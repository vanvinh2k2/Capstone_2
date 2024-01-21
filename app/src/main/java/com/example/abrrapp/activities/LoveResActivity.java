package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.LoveRestaurantAdapter;
import com.example.abrrapp.models.LikeRestaurant;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoveResActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView loveResrcv;
    LoveRestaurantAdapter loveResAdapter;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    List<LikeRestaurant> listRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_res);
        init();
        getToolBar();
        getLoveRestaurant();
    }

    private void getLoveRestaurant() {
        disposable.add(apiRestaurant.getListLikeRes(
                "user21aeae4cdh",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzA1ODkxMDQwLCJpYXQiOjE3MDU4MDQ2NDAsImp0aSI6ImE0ODFlNjI3NGRlYjQ0MmNhMzExNzkxZjk3NzY5MmMxIiwidXNlcl9pZCI6InVzZXIyMWFlYWU0Y2RoIn0.BqSI9sIH9fywAQhuzrgl4xpP7i7WvilPyoSH708S8zY"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        likeRestaurantModel -> {
                            if(likeRestaurantModel.isSuccess()){
                                listRes = likeRestaurantModel.getData();
                                loveResAdapter = new LoveRestaurantAdapter(R.layout.item_like_res, LoveResActivity.this, listRes);
                                loveResrcv.setAdapter(loveResAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(LoveResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
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
        loveResrcv = findViewById(R.id.loveRes);
        listRes = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}