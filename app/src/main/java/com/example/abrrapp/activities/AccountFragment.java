package com.example.abrrapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
    TextView usernametxt, nametxt, emailtxt, phonetxt, addresstxt, providertxt, datetxt;
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
        getProfile();
        getChart();
        return view;
    }

    // Tạo một danh sách các nhãn cho trục X
    private ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add(""); // Để dành cho vị trí 0
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");
        return labels;
    }
    private void getChart() {
        ArrayList<BarEntry> v = new ArrayList<>();
        v.add(new BarEntry(1, 42));
        v.add(new BarEntry(2, 12));
        v.add(new BarEntry(3, 22));
        v.add(new BarEntry(4, 62));
        v.add(new BarEntry(5, 22));
        v.add(new BarEntry(6, 62));
        v.add(new BarEntry(7, 12));
        v.add(new BarEntry(8, 10));
        v.add(new BarEntry(9, 38));
        v.add(new BarEntry(10, 32));
        v.add(new BarEntry(11, 72));
        v.add(new BarEntry(12, 13));

        BarDataSet barDataSet = new BarDataSet(v, "order");
        barDataSet.setColors(Color.GREEN);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);
        bar.setFitBars(true);
        bar.setData(barData);
        bar.getDescription().setText("month");
        bar.animateY(2000);
        XAxis xAxis = bar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Thiết lập vị trí của trục X là dưới cùng
        xAxis.setCenterAxisLabels(true); // Thiết lập các nhãn ở trung tâm của cột
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getLabels2()));
    }
    private ArrayList<String> getLabels2() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}