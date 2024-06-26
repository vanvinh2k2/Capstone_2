package com.example.abrrapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Rating;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.adapter.RestaurantAdapter;
import com.example.abrrapp.adapter.RestaurantHotAdapter;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.ReferenceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    RecyclerView listResRCV, listResHotRCV;
    RestaurantHotAdapter resHotAdapter;
    RestaurantAdapter resAdapter;
    ArrayList<Restaurant> listRes, listResHot;
    ArrayList<Rating> listRate;
    FloatingActionButton callphone;
    ImageView profileImage;
    private ProgressDialog progressDialog;
    Button searchbtn;
    EditText searchedt;
    ViewFlipper banner;
    APIRestaurant apiRestaurant;
    ReferenceManager manager;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        callPhone();
        getRating();
        actionViewFlipper();
        process();
        return view;
    }

    private void getRating(){
        disposable.add(apiRestaurant.getRatingAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ratingModel -> {
                            if(ratingModel.isSuccess()){
                                listRate = (ArrayList<Rating>) ratingModel.getData();
                                getRestaurantHot();
                                getRestaurant();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void process() {
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchedt.getText().length() > 0){
                    Intent intent = new Intent(getContext(), SearchResActivity.class);
                    intent.putExtra("q", searchedt.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }

    private void actionViewFlipper() {
        Picasso.get()
                .load(manager.getString("avatar"))
                .placeholder(R.drawable.user2)
                .into(profileImage);
        List<Integer> quangCao = new ArrayList<>();
        quangCao.add(R.drawable.banner);
        quangCao.add(R.drawable.banner2);
        quangCao.add(R.drawable.banner3);
        for(int i=0;i<quangCao.size();i++){
            ImageView anh = new ImageView(getContext());
            anh.setImageResource(quangCao.get(i));
            anh.setScaleType(ImageView.ScaleType.FIT_XY);
            banner.addView(anh);
        }
        banner.setFlipInterval(5000);
        banner.setAutoStart(true);
        Animation animationIn = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_right);
        Animation animationout = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_right);
        banner.setInAnimation(animationIn);
        banner.setOutAnimation(animationout);
    }

    private void callPhone() {
        callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 12);
                } else {
                    makePhoneCall();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0354342295"));
        startActivity(intent);
    }

    private void getRestaurant() {
        disposable.add(apiRestaurant.getRestaurant()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurantModel -> {
                            listRes = (ArrayList<Restaurant>) restaurantModel.getResults();
                            resAdapter = new RestaurantAdapter(R.layout.item_res2, listRes, listRate, getContext());
                            listResRCV.setAdapter(resAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getRestaurantHot() {
        disposable.add(apiRestaurant.getRestaurantHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurantModel -> {
                            listResHot = (ArrayList<Restaurant>) restaurantModel.getResults();
                            resHotAdapter = new RestaurantHotAdapter(R.layout.item_res, listResHot, listRate, getContext());
                            listResHotRCV.setAdapter(resHotAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void init(View view){
        listResRCV = view.findViewById(R.id.list_res);
        listResHotRCV = view.findViewById(R.id.list_res_hot);
        callphone = view.findViewById(R.id.phone);
        banner = view.findViewById(R.id.banner);
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
        listRes = new ArrayList<>();
        listResHot = new ArrayList<>();
        profileImage = view.findViewById(R.id.profile_image);
        searchbtn = view.findViewById(R.id.searchbtn);
        searchedt = view.findViewById(R.id.searchedt);
        manager = new ReferenceManager(getContext());
        listRate = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.setTitle("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }
}