package com.example.abrrapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.models.User;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountFragment extends Fragment {
    TabHost tabHost;
    TextView usernametxt, nametxt, emailtxt, phonetxt, addresstxt, providertxt, datetxt, totaltxt;
    ImageView logouttxt, changeProfiletxt;
    LinearLayout changePasswordtxt;
    ImageView image;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    ReferenceManager manager;
    BarChart bar;
    User profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        process();
        getStatistics();
        getProfile();
        return view;
    }

    private void getStatistics() {
        disposable.add(apiRestaurant.getStatistic(
                manager.getString("_id"),
                "Bearer "+manager.getString("access")
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        statisticModel -> {
                            if(statisticModel.isSuccess()){
                                ArrayList data = (ArrayList) statisticModel.getData().getNum_order();
                                getChart(data);
                                totaltxt.setText("Total expenses in the year: " + statisticModel.getData().getTotal() + "$");
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getChart(ArrayList data) {
        ArrayList<BarEntry> v = new ArrayList<>();
        v.add(new BarEntry(0, (Float) data.get(0)));
        v.add(new BarEntry(2, (Float) data.get(1)));
        v.add(new BarEntry(3, (Float) data.get(2)));
        v.add(new BarEntry(4, (Float) data.get(3)));
        v.add(new BarEntry(5, (Float) data.get(4)));
        v.add(new BarEntry(6, (Float) data.get(5)));
        v.add(new BarEntry(7, (Float) data.get(6)));
        v.add(new BarEntry(8, (Float) data.get(7)));
        v.add(new BarEntry(9, (Float) data.get(8)));
        v.add(new BarEntry(10, (Float) data.get(9)));
        v.add(new BarEntry(11, (Float) data.get(10)));
        v.add(new BarEntry(12, (Float) data.get(11)));
        BarDataSet barDataSet = new BarDataSet(v, "order");
        barDataSet.setColors(Color.GREEN);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(0f);

        BarData barData = new BarData(barDataSet);
        bar.setFitBars(true);
        bar.setData(barData);
        bar.getDescription().setText("month");
        bar.animateY(2000);

        XAxis xAxis = bar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set X-axis position at the bottom
        xAxis.setCenterAxisLabels(true); // Set the labels in the center of the columns
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getLabels()));
        // Adjust Y-axis to start at zero and remove any bottom padding
        YAxis leftAxis = bar.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Ensure the y-axis starts at zero
        leftAxis.setGranularity(1f); // Set granularity to 1
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawZeroLine(true); // Draw a zero line to make sure zero value aligns with the axis
        YAxis rightAxis = bar.getAxisRight();
        rightAxis.setAxisMinimum(0f); // Ensure the right y-axis also starts at zero
        rightAxis.setGranularity(1f); // Set granularity to 1
        rightAxis.setEnabled(false); // Disable the right axis if not needed
        bar.setExtraOffsets(0, 0, 0, 0);
    }
    private ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");
        return labels;
    }


    private void getProfile() {
        disposable.add(apiRestaurant.getProfile(
                        manager.getString("_id"),
                        "Bearer "+manager.getString("access")
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                User user = userModel.getData();
                                profile = user;
                                Picasso.get().load(user.getAvatar()).into(image);
                                usernametxt.setText(user.getUsername());
                                if(user.getFull_name() == null) nametxt.setText("Not Have");
                                else nametxt.setText(user.getFull_name());
                                if(user.getAddress() == null) nametxt.setText("Not Have");
                                else addresstxt.setText(user.getAddress());
                                datetxt.setText(user.getDate_joined());
                                if(user.getPhone() == null) nametxt.setText("Not Have");
                                else phonetxt.setText(user.getPhone());
                                providertxt.setText("Email");
                                emailtxt.setText(user.getEmail());
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void process() {
        if(manager.getString("provider").compareTo("Email")!=0)
            changePasswordtxt.setVisibility(View.GONE);
        changePasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        changeProfiletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangeProfileActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        });

        logouttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutGoogle();
                manager.clear();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signOutGoogle() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut();
    }

    public void init(View view){
        changePasswordtxt = view.findViewById(R.id.change_password);
        logouttxt = view.findViewById(R.id.logout);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec spec1, spec2;
        spec1 = tabHost.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("My profile");
        tabHost.addTab(spec1);

        spec2 = tabHost.newTabSpec("t2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Dashbroad");
        tabHost.addTab(spec2);
        usernametxt = view.findViewById(R.id.username);
        nametxt = view.findViewById(R.id.fullname);
        emailtxt = view.findViewById(R.id.email);
        phonetxt = view.findViewById(R.id.phone);
        addresstxt = view.findViewById(R.id.address);
        providertxt = view.findViewById(R.id.provider);
        datetxt = view.findViewById(R.id.date_joined);
        changeProfiletxt = view.findViewById(R.id.change_profile);
        image = view.findViewById(R.id.profile_image);
        manager = new ReferenceManager(getContext());
        bar = view.findViewById(R.id.barChart);
        totaltxt = view.findViewById(R.id.total);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}