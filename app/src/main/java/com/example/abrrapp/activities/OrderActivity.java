package com.example.abrrapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.OrderCartAdapter;
import com.example.abrrapp.models.OrderCart;
import com.example.abrrapp.models.OrderCartItem;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;

import java.util.logging.Logger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    TextView nameUsertxt, phonetxt, emailtxt, datetxt, timetxt, numPeople, tabletxt, subTotaltxt, totaltxt;
    RecyclerView orderrcv;
    OrderCartAdapter orderCartAdapter;
    Button paymentbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        getToolBar();
        getProcess();
        getOrder();
    }

    private void getProcess() {
        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BillActivity.class));
            }
        });
    }

    private void getOrder() {
        disposable.add(apiRestaurant.getOrderCart(
                "102566218799174938142",
                "resedh2aga5a3",
                Const.TOKEN
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderCartModel -> {
                            if(orderCartModel.isSuccess()){
                                OrderCart orderCart = orderCartModel.getData();
                                nameUsertxt.setText(orderCart.getOrder().getFull_name());
                                phonetxt.setText(orderCart.getOrder().getPhone());
                                emailtxt.setText(orderCart.getOrder().getUser().getEmail());
                                datetxt.setText(orderCart.getOrder().getOrder_date());
                                timetxt.setText(orderCart.getOrder().getTime_from().substring(0, 5)+
                                        " - "+orderCart.getOrder().getTime_to().substring(0, 5));
                                numPeople.setText(orderCart.getOrder().getNumber_people()+" peoples");
                                tabletxt.setText(orderCart.getOrder().getTable().getTitle());
                                subTotaltxt.setText("Sub Total: "+ orderCart.getOrder().getPrice()+"$");
                                totaltxt.setText("Total: "+(orderCart.getOrder().getPrice()-orderCart.getOrder().getDeposit())+"$");
                                orderCartAdapter = new OrderCartAdapter(R.layout.item_dish_order_2, getApplicationContext(), orderCart.getOrderDetail());
                                orderrcv.setAdapter(orderCartAdapter);

                            }else{
                                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
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

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        nameUsertxt = findViewById(R.id.name);
        phonetxt = findViewById(R.id.phone);
        emailtxt = findViewById(R.id.email);
        datetxt = findViewById(R.id.date);
        timetxt = findViewById(R.id.time);
        numPeople = findViewById(R.id.num_people);
        tabletxt = findViewById(R.id.table);
        orderrcv = findViewById(R.id.listOrder);
        subTotaltxt = findViewById(R.id.subtotal);
        totaltxt = findViewById(R.id.deposit);
        paymentbtn = findViewById(R.id.payment);
    }
}