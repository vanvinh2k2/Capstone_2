package com.example.abrrapp.activities;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.CategoryDropAdapter;
import com.example.abrrapp.adapter.DishFeatureAdapter;
import com.example.abrrapp.adapter.DishOfRestaurantAdapter;
import com.example.abrrapp.adapter.OrderCartAdapter;
import com.example.abrrapp.adapter.TableDropAdapter;
import com.example.abrrapp.adapter.TimeDropAdaper;
import com.example.abrrapp.models.Category;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.models.DishModel;
import com.example.abrrapp.models.ItemCart;
import com.example.abrrapp.models.OrderCart;
import com.example.abrrapp.models.OrderCartItem;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.models.Table;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailResActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView productimg, chatimg;
    List<Dish> listDish;
    TextView nametxt, ratingtxt, evaluatetxt, dishestxt, liketxt, opentxt, addresstxt,
            phonetxt, descriptiontxt, readtxt;
    RecyclerView dishFeaturercv;
    ImageView lovebtn, unlovebtn;
    Button nextbtn;
    Restaurant restaurant;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    String rid, usernameRes, titleRes, imageRes;
    DishFeatureAdapter dishFeatureAdapter;
    ReferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_res);
        init();
        getToolBar();
        getData();
        getFeatureDish();
        process();
        checkLike();
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
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void addLike(String rid){
        disposable.add(apiRestaurant.addLike(
                        manager.getString("_id"),
                        rid,
                        "Bearer " + manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()){
                                Toast.makeText(this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void checkLike(){
        disposable.add(apiRestaurant.checkLike(
                        manager.getString("_id"),
                        rid,
                        "Bearer " + manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()){
                                unlovebtn.setVisibility(View.VISIBLE);
                                lovebtn.setVisibility(View.GONE);
                            }else{
                                lovebtn.setVisibility(View.VISIBLE);
                                unlovebtn.setVisibility(View.GONE);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void process() {
        chatimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("usernameRes", usernameRes);
                intent.putExtra("imageRes", imageRes);
                intent.putExtra("titleRes", titleRes);
                startActivity(intent);
            }
        });

        lovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLike(rid);
                lovebtn.setVisibility(View.GONE);
                unlovebtn.setVisibility(View.VISIBLE);
                liketxt.setText((Integer.parseInt(liketxt.getText().toString()) - 1) +"");
            }
        });

        unlovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLike(rid);
                lovebtn.setVisibility(View.VISIBLE);
                unlovebtn.setVisibility(View.GONE);
                liketxt.setText((Integer.parseInt(liketxt.getText().toString()) + 1) +"");
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailResActivity.this, MenuActivity.class);
                intent.putExtra("rid", rid);
                startActivity(intent);
            }
        });

    }

    private void getFeatureDish() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            if(dishFeatureModel.isSuccess()){
                                listDish = dishFeatureModel.getData();
                                dishFeatureAdapter = new DishFeatureAdapter(R.layout.item_feartured_dish, DetailResActivity.this, listDish);
                                dishFeaturercv.setAdapter(dishFeatureAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        restaurant = (Restaurant) bundle.getSerializable("data");
        rid = restaurant.getRid();
        usernameRes = restaurant.getUsername();
        imageRes = restaurant.getImage();
        titleRes = restaurant.getTitle();
        Picasso.get().load(restaurant.getImage()).into(productimg);
        nametxt.setText(restaurant.getTitle());
        ratingtxt.setText("4 Rating");
        evaluatetxt.setText("45 Evaluate");
        dishestxt.setText("76 Dishes");
        liketxt.setText(restaurant.getLike()+"");
        opentxt.setText(restaurant.getTime_open().substring(0, 5)+" - "+restaurant.getTime_close().substring(0, 5));
        addresstxt.setText(restaurant.getAddress());
        phonetxt.setText(restaurant.getPhone());
        descriptiontxt.setText(Html.fromHtml(restaurant.getDescription()));
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
        productimg = findViewById(R.id.image);
        chatimg = findViewById(R.id.chat);
        nametxt = findViewById(R.id.title);
        ratingtxt = findViewById(R.id.rating);
        evaluatetxt = findViewById(R.id.evaluate);
        dishestxt = findViewById(R.id.num_dish);
        liketxt = findViewById(R.id.like);
        opentxt = findViewById(R.id.open);
        addresstxt = findViewById(R.id.address);
        phonetxt = findViewById(R.id.phone);
        descriptiontxt = findViewById(R.id.descript);
        readtxt = findViewById(R.id.read);
        listDish = new ArrayList<>();
        lovebtn = findViewById(R.id.love);
        unlovebtn = findViewById(R.id.un_love);
        nextbtn = findViewById(R.id.next);
        dishFeaturercv = findViewById(R.id.list_product);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(DetailResActivity.this);
    }
}