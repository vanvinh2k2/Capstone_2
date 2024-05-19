package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.RestaurantAdapter;
import com.example.abrrapp.models.Rating;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchResActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleTool;
    APIRestaurant apiRestaurant;
    ReferenceManager manager;
    CompositeDisposable disposable = new CompositeDisposable();
    ArrayList<Restaurant> listRes;
    RestaurantAdapter resAdapter;
    RecyclerView listResRCV;
    ArrayList<Rating> listRate;
    ImageView notfound;
    String q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);
        init();
        getToolBar();
        getData();
        getRating();
    }

    private void getData() {
        Intent intent = getIntent();
        q = intent.getStringExtra("q");
        titleTool.setText("Search with \"" + q + "\"");
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

    private void getRestaurant() {
        disposable.add(apiRestaurant.searchRestaurant(q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurantModel -> {
                            listRes = (ArrayList<Restaurant>) restaurantModel.getResults();
                            if(listRes.size() > 0){
                                resAdapter = new RestaurantAdapter(R.layout.item_res2, listRes, listRate, SearchResActivity.this);
                                listResRCV.setAdapter(resAdapter);
                            }
                            else notfound.setVisibility(View.VISIBLE);
                        },
                        throwable -> {
                            Toast.makeText(SearchResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getRating(){
        disposable.add(apiRestaurant.getRatingAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ratingModel -> {
                            if(ratingModel.isSuccess()){
                                listRate = (ArrayList<Rating>) ratingModel.getData();
                                getRestaurant();
                            }
                        },
                        throwable -> {
                            Toast.makeText(SearchResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        titleTool = findViewById(R.id.lbtoolbar);
        listRes = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(SearchResActivity.this);
        listResRCV = findViewById(R.id.list_res);
        notfound = findViewById(R.id.not_found);
        listRate = new ArrayList<>();
    }
}