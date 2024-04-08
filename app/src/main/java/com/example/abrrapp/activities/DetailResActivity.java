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
    TextView nametxt, ratingtxt, evaluatetxt, dishestxt, liketxt, opentxt, addresstxt,
            phonetxt, descriptiontxt, readtxt;
    RecyclerView dishrcv, dishFeaturercv;
    EditText nameUseredt, phoneUseredt, dateOrderedt, numPeopleedt, searchedt;
    Button nextbtn;
    ImageButton searchbtn;
    ImageView lovebtn, unlovebtn;
    Spinner tablespn, fromspn, tospn, categoryspn;
    Restaurant restaurant;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    String rid, usernameRes;
    List<Dish> listDish;
    List<Table> listTable;
    List<Category> listCategory;
    DishFeatureAdapter dishFeatureAdapter;
    TableDropAdapter tableDropAdapter;
    TimeDropAdaper timeToAdaper, timeFromAdaper;
    CategoryDropAdapter categoryDropAdapter;
    DishOfRestaurantAdapter dishOfRestaurantAdapter;
    public String idTable;
    public List<OrderCartItem> orderCartItems;
    ReferenceManager manager;
    boolean isCheck = false;

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
        process();
        getHistoryCart();
        checkLike();
    }

    private Integer getPositionTable(String tid, List<Table> tables){
        Integer position = 0;
        for (int i = 0; i < tables.size(); i++) {
            if (tid.compareTo(tables.get(i).getTid()) == 0) {
                position = i;
                break;
            }
        }
        return position;
    }

    private Integer getPositionTime(String time){
        Integer position = 0;
        for (int i = 0; i < Const.getTime().size(); i++) {
            if (time.compareTo(Const.getTime().get(i)) == 0) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void updateOrderCart(JSONArray jsonObject){
        disposable.add(
                apiRestaurant.updateOrderCart(
                                manager.getString("_id"),
                                rid,
                                nameUseredt.getText().toString().trim(),
                                phoneUseredt.getText().toString().trim(),
                                idTable,
                                Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                numPeopleedt.getText().toString().trim(),
                                jsonObject,
                                dateOrderedt.getText().toString().trim(),
                                "Bearer " + manager.getString("access")
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                defaultModel -> {
                                    if(defaultModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                                        intent.putExtra("rid", rid);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(DetailResActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DetailResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void addOrderCart(JSONArray jsonObject){
        disposable.add(
                apiRestaurant.addOrderCart(
                                manager.getString("_id"),
                                rid,
                                nameUseredt.getText().toString().trim(),
                                phoneUseredt.getText().toString().trim(),
                                idTable,
                                Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                numPeopleedt.getText().toString().trim(),
                                jsonObject,
                                dateOrderedt.getText().toString().trim(),
                                "Bearer " + manager.getString("access")
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                defaultModel -> {
                                    if(defaultModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                                        intent.putExtra("rid", rid);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(DetailResActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(DetailResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private void getHistoryCart() {
        disposable.add(apiRestaurant.getOrderCart(
                    manager.getString("_id"),
                    rid,
                    "Bearer " + manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderCartModel -> {
                            if(orderCartModel.isSuccess()){
                                OrderCart orderCart = orderCartModel.getData();
                                nameUseredt.setText(orderCart.getOrder().getFull_name());
                                phoneUseredt.setText(orderCart.getOrder().getPhone());
                                dateOrderedt.setText(orderCart.getOrder().getOrder_date());
                                numPeopleedt.setText(orderCart.getOrder().getNumber_people() + "");
                                orderCartItems = orderCart.getOrderDetail();
                                tospn.setSelection(getPositionTime(orderCart.getOrder().getTime_to().toString().substring(0,5)));
                                fromspn.setSelection(getPositionTime(orderCart.getOrder().getTime_from().toString().substring(0,5)));
                                tablespn.setSelection(getPositionTable(orderCart.getOrder().getTable().getTid(),listTable));
                            }else {
                                isCheck = true;
                                Log.e("data","er");
                            }
                        },
                        throwable -> {
                            Log.e("data",throwable.getMessage());
                        }
                ));
    }

    public boolean checkInput(){
        if(nameUseredt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phoneUseredt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field phone!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(dateOrderedt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field date order!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(numPeopleedt.getText().toString().trim().length()<=0){
            Toast.makeText(this, "Please input field number people!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(idTable.compareTo("0")==0){
            Toast.makeText(this, "Please choice table!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(fromspn.getSelectedItemPosition()==0){
            Toast.makeText(this, "Please choice time from!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(tospn.getSelectedItemPosition()==0){
            Toast.makeText(this, "Please choice time to!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

        dateOrderedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int ngay,thang,nam;
                ngay = calendar.get(Calendar.DATE);
                thang = calendar.get(Calendar.MONTH);
                nam = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailResActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateOrderedt.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },nam,thang,ngay);
                datePickerDialog.show();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    JSONArray jsonObject = new JSONArray();
                    for(int i = 0;i < orderCartItems.size();i++){
                        JSONObject item = new JSONObject();
                        try {
                            item.put("did", orderCartItems.get(i).getDish().getDid());
                            item.put("quantity", orderCartItems.get(i).getQuantity());
                            jsonObject.put(item);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    disposable.add(apiRestaurant.checkOrder(
                                        rid,
                                        Const.timeOrder.get(fromspn.getSelectedItemPosition()),
                                        Const.timeOrder.get(tospn.getSelectedItemPosition()),
                                        idTable,
                                        "Bearer " + manager.getString("access")

                                ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        defaultModel -> {
                                            if(defaultModel.isSuccess()){
                                                if(isCheck){
                                                    addOrderCart(jsonObject);
                                                } else {
                                                    updateOrderCart(jsonObject);
                                                }
                                            }else {
                                                Toast.makeText(DetailResActivity.this, defaultModel.getMessage()+"", Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(DetailResActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                                        }
                                ));
                }
            }
        });
    }

    private void getDishOfRes() {
        disposable.add(apiRestaurant.getFeatureDish(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishFeatureModel -> {
                            if(dishFeatureModel.isSuccess()){
                                listDish = dishFeatureModel.getData();
                                dishOfRestaurantAdapter = new DishOfRestaurantAdapter(R.layout.item_dish_order, DetailResActivity.this, listDish);
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
        timeToAdaper = new TimeDropAdaper(R.layout.item_list_drop_down, DetailResActivity.this, Const.getTime());
        timeFromAdaper = new TimeDropAdaper(R.layout.item_list_drop_down, DetailResActivity.this, Const.getTime());
        fromspn.setAdapter(timeFromAdaper);
        tospn.setAdapter(timeToAdaper);
    }

    public void getDropTable() {
        disposable.add(apiRestaurant.getTable(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tableModel -> {
                            if(tableModel.isSuccess()){
                                listTable.add(new Table("0", "Choice table"));
                                listTable.addAll(tableModel.getData());
                                tableDropAdapter = new TableDropAdapter(R.layout.item_list_drop_down, DetailResActivity.this, listTable);
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
        lovebtn = findViewById(R.id.love);
        unlovebtn = findViewById(R.id.un_love);
        dishFeaturercv = findViewById(R.id.list_product);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        orderCartItems = new ArrayList<>();
        manager = new ReferenceManager(DetailResActivity.this);
    }
}