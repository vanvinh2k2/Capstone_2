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
import com.example.abrrapp.models.ItemOrder;
import com.example.abrrapp.models.OrderCart;
import com.example.abrrapp.models.OrderCartItem;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.paypal.android.sdk.payments.PayPalPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    TextView nameUsertxt, phonetxt, emailtxt, datetxt, timetxt,
            numPeople, tabletxt, subTotaltxt, totaltxt;
    RecyclerView orderrcv;
    OrderCartAdapter orderCartAdapter;
    Button paymentbtn;
    ReferenceManager manager;
    PayPalPayment payPalPayment;
    String rid, fullName, phone, tid, timeFrom, timeTo, dateOrder;
    float deposite;
    int numberPeople;
    List<OrderCartItem> orderCartItemList;
    List<ItemOrder> itemOrderList;
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
                if(totalPrice(orderCartItemList)<=100){
                    payment();
                }else{
                    payment();
                }
            }
        });
    }

    private void payment(){
        startActivity(new Intent(getApplicationContext(), BillActivity.class));

        /*disposable.add(apiRestaurant.addOrder(
                        manager.getString("_id"),
                        rid,
                        fullName,
                        phone,
                        tid,
                        deposite,
                        deposite * 100 / 10,
                        timeFrom,
                        timeTo,
                        numberPeople,
                        "",
                        dateOrder,
                        manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()){
                                Toast.makeText(OrderActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(), BillActivity.class));
                            }
                        },
                        throwable -> {
                            Toast.makeText(OrderActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));*/
    }

    private float totalPrice(List<OrderCartItem> orderCartItems){
        float price = 0;
        for(int i=0;i<orderCartItems.size();i++){
            price += orderCartItems.get(i).getQuantity()*orderCartItems.get(i).getDish().getPrice();
        }
        return price;
    }

    public void deleteDish(int i){
        orderCartItemList.remove(i);
        orderCartAdapter.notifyDataSetChanged();
        subTotaltxt.setText("Total: "+ totalPrice(orderCartItemList) +"$");
        deposite = totalPrice(orderCartItemList)*10/100;
        totaltxt.setText("Deposite: "+ deposite +"$");
    }

    private void getOrder() {
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
                                nameUsertxt.setText(orderCart.getOrder().getFull_name());
                                fullName = orderCart.getOrder().getFull_name();
                                phonetxt.setText(orderCart.getOrder().getPhone());
                                phone = orderCart.getOrder().getPhone();
                                emailtxt.setText(orderCart.getOrder().getUser().getEmail());
                                datetxt.setText(orderCart.getOrder().getOrder_date());
                                dateOrder = orderCart.getOrder().getOrder_date();
                                timetxt.setText(orderCart.getOrder().getTime_from().substring(0, 5)+
                                        " - "+orderCart.getOrder().getTime_to().substring(0, 5));
                                timeFrom = orderCart.getOrder().getTime_from();
                                timeTo = orderCart.getOrder().getTime_to();
                                numPeople.setText(orderCart.getOrder().getNumber_people()+" peoples");
                                numberPeople = orderCart.getOrder().getNumber_people();
                                tabletxt.setText(orderCart.getOrder().getTable().getTitle());
                                tid = orderCart.getOrder().getTable().getTid();
                                orderCartItemList = orderCart.getOrderDetail();
                                subTotaltxt.setText("Total: "+ totalPrice(orderCart.getOrderDetail()) +"$");
                                deposite = totalPrice(orderCart.getOrderDetail())*10/100;
                                totaltxt.setText("Deposite: "+ deposite +"$");



                                orderCartAdapter = new OrderCartAdapter(R.layout.item_dish_order_2, OrderActivity.this, orderCart.getOrderDetail());
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
        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");
    }

    private List<ItemOrder> itemOrders(List<OrderCartItem> orderCartItems){
        List<ItemOrder> itemOrderList = new ArrayList<>();



        return itemOrderList;
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
        manager = new ReferenceManager(OrderActivity.this);
        orderCartItemList = new ArrayList<>();
        itemOrderList = new ArrayList<>();
    }
}