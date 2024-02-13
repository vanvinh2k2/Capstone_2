package com.example.abrrapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.RestaurantAdapter;
import com.example.abrrapp.models.Restaurant;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchAIFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_a_i, container, false);
        return view;
    }

    /*private void getRestaurant() {
        disposable.add(apiRestaurant.getRestaurant()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurantModel -> {
                            listRes = (ArrayList<Restaurant>) restaurantModel.getResults();
                            resAdapter = new RestaurantAdapter(R.layout.item_res2, listRes, getContext());
                            listResRCV.setAdapter(resAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                ));
    }*/
}