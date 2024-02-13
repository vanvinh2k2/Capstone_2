package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.OrderHistoryAdapter;
import com.example.abrrapp.models.Order;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView orderrcv;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    List<Order> listOrder;
    OrderHistoryAdapter orderHistoryAdapter;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        init();
        getToolBar();
        getHistoryOrder();
    }

    private void getHistoryOrder() {
        disposable.add(apiRestaurant.getListOrder(
                        manager.getString("_id"),
                        manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            if(orderModel.isSuccess()){
                                listOrder = orderModel.getData();
                                orderHistoryAdapter = new OrderHistoryAdapter(R.layout.item_order, getApplicationContext(), listOrder);
                                orderrcv.setAdapter(orderHistoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(HistoryOrderActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
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
        orderrcv = findViewById(R.id.list_order);
        listOrder = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        manager = new ReferenceManager(getApplicationContext());
    }
}