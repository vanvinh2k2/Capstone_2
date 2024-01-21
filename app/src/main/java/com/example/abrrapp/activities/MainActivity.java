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

import com.example.abrrapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FrameLayout loveRes, historyOrder;
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

    private void init(){
        bottomNavigation = findViewById(R.id.menu_nav);
        loveRes = findViewById(R.id.loveRes);
        historyOrder = findViewById(R.id.historyOrder);
    }
}