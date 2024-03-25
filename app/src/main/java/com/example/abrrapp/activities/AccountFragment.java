package com.example.abrrapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.models.User;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountFragment extends Fragment {
    TabHost tabHost;
    TextView changePasswordtxt, usernametxt, uidtxt, nametxt,
            emailtxt, phonetxt, addresstxt, providertxt, datetxt;
    Button logoutbtn, changeProfilebtn;
    ImageView image;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    ReferenceManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        process();
        getProfile();
        return view;
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
                                Picasso.get().load(user.getAvatar()).into(image);
                                usernametxt.setText(user.getUsername());
                                if(user.getFull_name() == null) nametxt.setText("Not Have");
                                else nametxt.setText(user.getFull_name());
                                uidtxt.setText(user.getId());
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
        changePasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.clear();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init(View view){
        changePasswordtxt = view.findViewById(R.id.change_password);
        logoutbtn = view.findViewById(R.id.logout);
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
        uidtxt = view.findViewById(R.id.uid);
        nametxt = view.findViewById(R.id.fullname);
        emailtxt = view.findViewById(R.id.email);
        phonetxt = view.findViewById(R.id.phone);
        addresstxt = view.findViewById(R.id.address);
        providertxt = view.findViewById(R.id.provider);
        datetxt = view.findViewById(R.id.date_joined);
        changeProfilebtn = view.findViewById(R.id.change_profile);
        image = view.findViewById(R.id.profile_image);
        manager = new ReferenceManager(getContext());
    }
}