package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.OrderDishHistoryAdapter;
import com.example.abrrapp.models.Order;
import com.example.abrrapp.models.OrderItem;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailHistoryOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView nametxt, phonetxt, emailtxt, datetxt, timetxt, numPeopletxt, tabletxt,
    subTotaltxt, depositedtxt, totaltxt, statustxt;
    Button cancelbtn;
    boolean iscancel = true;
    RecyclerView orderrcv;
    OrderDishHistoryAdapter orderDishHistoryAdapter;
    List<OrderItem> listOrderItem;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    Order order;
    HashMap<String, String> arr;
    String oid;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_order);
        init();
        getToolBar();
        getData();
        getDishOrder();
        process();
    }

    private void process() {
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iscancel){
                    disposable.add(apiRestaurant.cancelOrder(
                                    oid,
                                    manager.getString("access")
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    defaultModel -> {
                                        if(defaultModel.isSuccess()){
                                            cancelbtn.setBackgroundColor(Color.GRAY);
                                            iscancel = false;
                                            statustxt.setText("Status order: Cancel");
                                            Toast.makeText(DetailHistoryOrderActivity.this, "Successful.", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(DetailHistoryOrderActivity.this, "Cancel fail!", Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }else{
                    Toast.makeText(DetailHistoryOrderActivity.this, "Cancel fail!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDishOrder() {
        disposable.add(apiRestaurant.getOrderDetail(
                oid,
                manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderItemModel -> {
                            if(orderItemModel.isSuccess()){
                                listOrderItem = orderItemModel.getData();
                                orderDishHistoryAdapter = new OrderDishHistoryAdapter(R.layout.item_history_order,
                                        getApplicationContext(), listOrderItem);
                                orderrcv.setAdapter(orderDishHistoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
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

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        order = (Order) bundle.getSerializable("data");
        oid = order.getOid();
        nametxt.setText(order.getFull_name());
        phonetxt.setText(order.getPhone());
        emailtxt.setText(order.getUser().getEmail());
        datetxt.setText(order.getOrder_date());
        timetxt.setText(order.getTime_from().substring(0, 5)+" - "+order.getTime_to().substring(0, 5));
        numPeopletxt.setText(order.getNumber_people()+" peoples");
        tabletxt.setText(order.getTable().getTitle());
        subTotaltxt.setText("SubTotal: "+order.getPrice()+"$");
        depositedtxt.setText("Diposited: "+order.getDeposit()+"$");
        float total = order.getPrice()-order.getDeposit();
        totaltxt.setText("Grand Total: "+total+"$");
        statustxt.setText("Status order: " + arr.get(order.getProduct_status()));
        if(order.getDeposit() != 0){
            cancelbtn.setBackgroundColor(Color.GRAY);
            iscancel = false;
        }
        if(order.getProduct_status().compareTo("cancel") == 0){
            iscancel = false;
            cancelbtn.setBackgroundColor(Color.GRAY);
        }
    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        nametxt = findViewById(R.id.name);
        phonetxt = findViewById(R.id.phone);
        emailtxt = findViewById(R.id.email);
        datetxt = findViewById(R.id.date);
        timetxt = findViewById(R.id.time);
        numPeopletxt = findViewById(R.id.num_people);
        tabletxt = findViewById(R.id.table);
        subTotaltxt = findViewById(R.id.subtotal);
        depositedtxt = findViewById(R.id.deposit);
        totaltxt = findViewById(R.id.total);
        statustxt = findViewById(R.id.status);
        cancelbtn = findViewById(R.id.cancel);
        orderrcv = findViewById(R.id.listOrder);
        listOrderItem = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        arr = new HashMap<>();
        arr.put("awaiting_confirmation", "Awaiting confirmation");
        arr.put("confirmed", "Confirmed");
        arr.put("complete", "Complete");
        arr.put("cancel", "Cancel");
        manager = new ReferenceManager(getApplicationContext());
    }
}