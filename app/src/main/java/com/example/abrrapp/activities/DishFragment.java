package com.example.abrrapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.DishAdapter;
import com.example.abrrapp.adapter.ResDropAdapter;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.retrofit.APIRestaurant;
import com.example.abrrapp.retrofit.RetrofitClient;
import com.example.abrrapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishFragment extends Fragment {
    EditText searchedt;
    ImageButton searchbtn;
    RecyclerView dishrcl;
    DishAdapter dishAdapter;
    List<Dish> listDish;
    APIRestaurant apiRestaurant;
    CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish, container, false);
        init(view);
        getDish();
        return view;
    }

    private void getDish() {
        disposable.add(apiRestaurant.getDish()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dishModel -> {
                            listDish = dishModel.getResults();
                            dishAdapter = new DishAdapter(R.layout.item_dish, getContext(), listDish);
                            dishrcl.setAdapter(dishAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void init(View view){
        searchbtn = view.findViewById(R.id.search_dishbtn);
        searchedt = view.findViewById(R.id.search_dish);
        dishrcl = view.findViewById(R.id.list_dish);
        listDish = new ArrayList<>();
        apiRestaurant = RetrofitClient.getInstance(Const.BASE_URL).create(APIRestaurant.class);
    }
}