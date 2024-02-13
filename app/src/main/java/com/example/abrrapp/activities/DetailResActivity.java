package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
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
import com.example.abrrapp.adapter.TableDropAdapter;
import com.example.abrrapp.adapter.TimeDropAdaper;
import com.example.abrrapp.models.Category;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.models.DishModel;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.models.Table;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailResActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView productimg, chatimg;
    TextView nametxt, ratingtxt, evaluatetxt, dishestxt, liketxt, opentxt, addresstxt,
            phonetxt, descriptiontxt, readtxt;
    RecyclerView dishrcv, dishFeaturercv;
    EditText nameUseredt, phoneUseredt, dateOrderedt, numPeopleedt, searchedt;
    Button nextbtn;
    ImageButton searchbtn;
    Spinner tablespn, fromspn, tospn, categoryspn;
    Restaurant restaurant;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    String rid;
    List<Dish> listDish;
    List<Table> listTable;
    List<Category> listCategory;
    DishFeatureAdapter dishFeatureAdapter;
    TableDropAdapter tableDropAdapter;
    TimeDropAdaper timeDropAdaper;
    CategoryDropAdapter categoryDropAdapter;
    DishOfRestaurantAdapter dishOfRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_res);
        init();
        getToolBar();
        getData();
        getFeatureDish();
        getDropTable();
        getDropTime();
        getDropCategory();
        getDishOfRes();
    }

    private void getDishOfRes() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            if(dishFeatureModel.isSuccess()){
                                listDish = dishFeatureModel.getData();
                                dishOfRestaurantAdapter = new DishOfRestaurantAdapter(R.layout.item_dish_order, getApplicationContext(), listDish);
                                dishrcv.setAdapter(dishOfRestaurantAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getDropCategory() {
        disposable.add(apiRestaurant.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if(categoryModel.isSuccess()){
                                listCategory.add(new Category("0", "All category"));
                                listCategory.addAll(categoryModel.getData());
                                categoryDropAdapter = new CategoryDropAdapter(R.layout.item_list_drop_down, getApplicationContext(), listCategory);
                                categoryspn.setAdapter(categoryDropAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getDropTime() {
        timeDropAdaper = new TimeDropAdaper(R.layout.item_list_drop_down, getApplicationContext(), Const.getTime());
        fromspn.setAdapter(timeDropAdaper);
        tospn.setAdapter(timeDropAdaper);
    }

    private void getDropTable() {
        disposable.add(apiRestaurant.getTable(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tableModel -> {
                            if(tableModel.isSuccess()){
                                listTable.add(new Table("0", "Choice table"));
                                listTable.addAll(tableModel.getData());
                                tableDropAdapter = new TableDropAdapter(R.layout.item_list_drop_down, getApplicationContext(), listTable);
                                tablespn.setAdapter(tableDropAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getFeatureDish() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            if(dishFeatureModel.isSuccess()){
                                listDish = dishFeatureModel.getData();
                                dishFeatureAdapter = new DishFeatureAdapter(R.layout.item_feartured_dish, getApplicationContext(), listDish);
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
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
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
        dishrcv = findViewById(R.id.list_menu);
        nameUseredt = findViewById(R.id.name_user);
        phoneUseredt = findViewById(R.id.phone_user);
        dateOrderedt = findViewById(R.id.date_order);
        numPeopleedt = findViewById(R.id.num_people);
        searchedt = findViewById(R.id.searchedt);
        searchbtn = findViewById(R.id.searchbtn);
        nextbtn = findViewById(R.id.next);
        tablespn = findViewById(R.id.table);
        fromspn = findViewById(R.id.time_from);
        tospn = findViewById(R.id.time_to);
        categoryspn = findViewById(R.id.category);
        listDish = new ArrayList<>();
        listTable = new ArrayList<>();
        listCategory = new ArrayList<>();
        dishFeaturercv = findViewById(R.id.list_product);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}