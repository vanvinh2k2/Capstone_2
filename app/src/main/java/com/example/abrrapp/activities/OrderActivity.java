package com.example.abrrapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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
    public PayPalConfiguration payPalConfiguration;
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
        payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId(Const.YOUR_CLIENT_ID);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);
        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalPrice(orderCartItemList)<=100){
                    payment();
                }else{
                    getPayPal();
                }
            }
        });
    }

    private void getPayPal(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(deposite), "USD","Tổng thanh toán", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent,1);
    }

    private void deleteOrderCart(){
        disposable.add(apiRestaurant.deleteOrderCart(
                manager.getString("_id"),
                rid
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()) startActivity(new Intent(getApplicationContext(), BillActivity.class));
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void payment(){
        Gson gson = new Gson();
        float deposite2 = (float) (Math.round((deposite * 100 / 10)*100)/100.00);
        disposable.add(apiRestaurant.addOrder(
                        manager.getString("_id"),
                        rid,
                        fullName,
                        phone,
                        tid,
                        deposite,
                        deposite2,
                        timeFrom,
                        timeTo,
                        numberPeople,
                        gson.toJson(itemOrders(orderCartItemList)),
                        dateOrder,
                        "Bearer "+manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        defaultModel -> {
                            if(defaultModel.isSuccess()) deleteOrderCart();
                        },
                        throwable -> {
                            Toast.makeText(OrderActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(paymentConfirmation != null){
                try {
                    String pamentdetail = paymentConfirmation.toJSONObject().toString();
                    JSONObject jsonObject = new JSONObject(pamentdetail);
                    payment();
                    Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(this, e.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode== Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        for(int i=0;i<orderCartItems.size();i++){
            OrderCartItem orderItem = orderCartItems.get(i);
            float total = orderItem.getQuantity()*orderItem.getDish().getPrice();
            ItemOrder item = new ItemOrder(
                    orderItem.getDish().getDid(),
                    orderItem.getDish().getImage(),
                    orderItem.getQuantity(),
                    orderItem.getDish().getPrice(),
                    total);
            itemOrderList.add(item);
        }
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