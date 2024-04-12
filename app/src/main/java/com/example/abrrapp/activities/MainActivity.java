package com.example.abrrapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FrameLayout loveRes, historyOrder;
    TextView liketxt, ordertxt;
    ReferenceManager manager;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        process();
    }

    private void process() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.home){
                    transaction.replace(R.id.container, new HomeFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.dishes) {
                    transaction.replace(R.id.container, new DishFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.search) {
                    transaction.replace(R.id.container, new SearchAIFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.account) {
                    transaction.replace(R.id.container, new AccountFragment());
                    item.setChecked(true);
                }else{
                    transaction.replace(R.id.container, new ContactUsFragment());
                    item.setChecked(true);
                }
                transaction.commit();
                return false;
            }
        });

        loveRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoveResActivity.class);
                startActivity(intent);
            }
        });

        historyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        disposable.add(apiRestaurant.num(manager.getString("_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        numModel -> {
                            if(numModel.isSuccess()){
                                liketxt.setText(numModel.getData().getNum_like()+"");
                                ordertxt.setText(numModel.getData().getNum_order()+"");
                            }
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
        super.onResume();
    }

    private void init(){
        bottomNavigation = findViewById(R.id.menu_nav);
        loveRes = findViewById(R.id.loveRes);
        historyOrder = findViewById(R.id.historyOrder);
        liketxt = findViewById(R.id.count_love);
        ordertxt = findViewById(R.id.count_his);
        manager = new ReferenceManager(this);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}