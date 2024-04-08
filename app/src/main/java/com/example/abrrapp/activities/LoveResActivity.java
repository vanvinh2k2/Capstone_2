package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.LoveRestaurantAdapter;
import com.example.abrrapp.models.LikeRestaurant;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

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
    ReferenceManager manager;
    ImageView emptyimg;
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
                    manager.getString("_id"),
                    "Bearer " + manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        likeRestaurantModel -> {
                            if(likeRestaurantModel.isSuccess()){
                                listRes = likeRestaurantModel.getData();
                                if(listRes.size()>0) {
                                    loveResAdapter = new LoveRestaurantAdapter(R.layout.item_like_res, LoveResActivity.this, listRes);
                                    loveResrcv.setAdapter(loveResAdapter);
                                }else emptyimg.setVisibility(View.VISIBLE);
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

    public void deleteLike(String rid){
        disposable.add(apiRestaurant.delLike(
                        manager.getString("_id"),
                        rid,
                        "Bearer " + manager.getString("access")
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        likeRestaurantModel -> {
                            if(likeRestaurantModel.isSuccess()){
                                Toast.makeText(this, likeRestaurantModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                loveResAdapter.setListRes(likeRestaurantModel.getData());
                                loveResAdapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        loveResrcv = findViewById(R.id.loveRes);
        listRes = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(getApplicationContext());
        emptyimg = findViewById(R.id.empty);
    }
}