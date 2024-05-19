package com.example.abrrapp.activities;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.ProvinceAdapter;
import com.example.abrrapp.adapter.RestaurantAdapter;
import com.example.abrrapp.models.Province;
import com.example.abrrapp.models.Rating;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;
import com.example.abrrapp.utils.ReferenceManager;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SearchAIFragment extends Fragment {
    ImageView searchimg, noimg, noteimg;
    ImageButton camerabtn, colectionbtn;
    Spinner addressspn;
    Button searchbtn;
    TextView resulttxt, titletxt;
    Bitmap anh;
    int CodeCamera = 123,CodeColection = 125;
    ProvinceAdapter provinceAdapter;
    private LocationManager locationManager;
    RestaurantAdapter resAdapter, resSearchAdapter;
    RecyclerView listResrcv, listResCurrentrcv;
    APIRestaurant apiRestaurant;
    ArrayList<Restaurant> listRes, listSearchRes;
    ReferenceManager manager;
    ArrayList<Rating> listRate;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_a_i, container, false);
        init(view);
        getRating();
        getProvince();
        getLocation();
        process();
        return view;
    }

    private void getLocation(){
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Nếu không có quyền, yêu cầu người dùng cấp quyền
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        // Lấy vị trí hiện tại
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Gson g = new Gson(); 

        if (location != null) {
            // Hiển thị vị trí
            showLocation(location);
        }
        // Đăng ký người nghe vị trí mới
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Khi vị trí thay đổi, hiển thị vị trí mới
                showLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }

    private void showLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // Hiển thị vị trí dưới dạng địa chỉ
        try {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                Toast.makeText(requireContext(), "Your location: " + address, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRating(){
        disposable.add(apiRestaurant.getRatingAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ratingModel -> {
                            if(ratingModel.isSuccess()){
                                listRate = (ArrayList<Rating>) ratingModel.getData();
                                getRestaurantCurrent();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void process() {
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CodeCamera);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CodeCamera);
                }
            }
        });

        colectionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CodeCamera);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,CodeColection);
                }
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAI();
            }
        });
    }

    private void searchAI() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        anh.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        disposable.add(apiRestaurant.searchAI(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        searchAIModel -> {
                            if(searchAIModel.isSuccess()){
                                resulttxt.setVisibility(View.VISIBLE);
                                titletxt.setVisibility(View.VISIBLE);
                                if(searchAIModel.getData().getResult().compareTo("") == 0) {
                                    resulttxt.setText("Unable to identify");
                                    noimg.setVisibility(View.VISIBLE);
                                    listResrcv.setVisibility(View.GONE);
                                }
                                else resulttxt.setText(searchAIModel.getData().getResult());
                                listSearchRes.clear();
                                for(int i=0;i<searchAIModel.getData().getRestaurant().size();i++)
                                    listSearchRes.add(searchAIModel.getData().getRestaurant().get(i).getRestaurant());
                                if(listSearchRes.size()>0){
                                    resSearchAdapter = new RestaurantAdapter(R.layout.item_res2, listSearchRes, listRate, getContext());
                                    listResrcv.setAdapter(resSearchAdapter);
                                    noimg.setVisibility(View.GONE);
                                    listResrcv.setVisibility(View.VISIBLE);
                                }else{
                                    noimg.setVisibility(View.VISIBLE);
                                    listResrcv.setVisibility(View.GONE);
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CodeCamera){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CodeCamera);
            }
            else Toast.makeText(getActivity(), "Ban ko cho phép mở Camera", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == CodeColection){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,CodeColection);
            }
            else Toast.makeText(getActivity(), "Bạn ko cho phép truy cập vào thư viện", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu người dùng cấp quyền, tiếp tục lấy vị trí
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) showLocation(location);
            } else Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CodeCamera) {
                if (data != null && data.getExtras() != null) {
                    anh = (Bitmap) data.getExtras().get("data");
                    noteimg.setVisibility(View.GONE);
                    searchimg.setImageBitmap(anh);
                }
            } else if (requestCode == CodeColection) {
                if (data != null && data.getData() != null) {
                    Uri url = data.getData();
                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(url);
                        anh = BitmapFactory.decodeStream(inputStream);
                        noteimg.setVisibility(View.GONE);
                        searchimg.setImageBitmap(anh);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void getProvince() {
        disposable.add(apiRestaurant.getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        provinceModel -> {
                            List<Province> listData = new ArrayList<>();
                            listData.add(new Province("0", "Your position"));
                            listData.add(new Province("1", "Display all"));
                            listData.addAll(provinceModel.getResults());
                            provinceAdapter = new ProvinceAdapter(R.layout.item_list_drop_down, getContext(), listData);
                            addressspn.setAdapter(provinceAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getRestaurantCurrent() {
        disposable.add(apiRestaurant.getRestaurantHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurantModel -> {
                            listRes = (ArrayList<Restaurant>) restaurantModel.getResults();
                            resAdapter = new RestaurantAdapter(R.layout.item_res2, listRes, listRate, getContext());
                            listResCurrentrcv.setAdapter(resAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void init(View view){
        searchimg = view.findViewById(R.id.image_search);
        camerabtn = view.findViewById(R.id.camera);
        colectionbtn = view.findViewById(R.id.colection);
        addressspn = view.findViewById(R.id.address);
        searchbtn = view.findViewById(R.id.confirm);
        listRes = new ArrayList<>();
        listSearchRes = new ArrayList<>();
        resulttxt = view.findViewById(R.id.result);
        listResrcv = view.findViewById(R.id.list_res);
        titletxt = view.findViewById(R.id.title);
        listResCurrentrcv = view.findViewById(R.id.list_res_current);
        noimg = view.findViewById(R.id.not_found);
        noteimg = view.findViewById(R.id.note);
        manager = new ReferenceManager(getContext());
        listRate = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}