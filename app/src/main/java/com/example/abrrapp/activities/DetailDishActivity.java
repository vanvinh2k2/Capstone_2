package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.DishAdapter;
import com.example.abrrapp.adapter.DishOfRestaurantAdapter;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailDishActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView image;
    TextView titletxt, nameRestxt, pricetxt, saletxt, descriptiontxt;
    Button viewbtn;
    RecyclerView dishrcv;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    String rid;
    DishAdapter dishAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dish);
        init();
        getToolBar();
        getData();
        getDishOfRestaurant();
    }

    private void getDishOfRestaurant() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            dishAdapter = new DishAdapter(R.layout.item_dish, getApplicationContext(), dishFeatureModel.getData());
                            dishrcv.setAdapter(dishAdapter);
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        Dish dish = (Dish) bundle.getSerializable("data");
        rid = dish.getRestaurant().getRid();
        titletxt.setText(dish.getTitle());
        nameRestxt.setText(dish.getRestaurant().getTitle());
        pricetxt.setText(dish.getPrice()+"$");
        saletxt.setText("10%");
        descriptiontxt.setText(dish.getDescription());
        Picasso.get().load(dish.getImage()).into(image);
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disposable.add(apiRestaurant.getDetailRestaurant(rid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                detailRestaurantModel -> {
                                    if(detailRestaurantModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), DetailResActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("data", detailRestaurantModel.getData());
                                        intent.putExtra("bundle", bundle);
                                        startActivity(intent);
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DetailDishActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        });
    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        image = findViewById(R.id.image);
        titletxt = findViewById(R.id.title);
        nameRestxt = findViewById(R.id.name_res);
        pricetxt = findViewById(R.id.price);
        saletxt = findViewById(R.id.sale);
        descriptiontxt = findViewById(R.id.descript);
        viewbtn = findViewById(R.id.view);
        dishrcv = findViewById(R.id.list_res);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);

    }
}