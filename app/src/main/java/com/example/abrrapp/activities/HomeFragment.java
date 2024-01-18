package com.example.abrrapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.RestaurantAdapter;
import com.example.abrrapp.adapter.RestaurantHotAdapter;
import com.example.abrrapp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView listResRCV, listResHotRCV;
    RestaurantHotAdapter resHotAdapter;
    RestaurantAdapter resAdapter;
    ArrayList<Restaurant> listRes, listResHot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        getRestaurantHot();
        getRestaurant();
        return view;
    }

    private void getRestaurant() {
        listRes.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listRes.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listRes.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listRes.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        resAdapter = new RestaurantAdapter(R.layout.item_res2, listRes, getContext());
        listResRCV.setAdapter(resAdapter);
    }

    private void getRestaurantHot() {
        listResHot.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listResHot.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listResHot.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        listResHot.add(new Restaurant("resedh2aga5a3","New Restaurant",
                "http://127.0.0.1:8000/media/restaurant_resedh2aga5a3/nhaan.png",
                "07:00", "Duy Xuyen, Quang Nam","22:00"));
        resHotAdapter = new RestaurantHotAdapter(R.layout.item_res, listResHot, getContext());
        listResHotRCV.setAdapter(resHotAdapter);
    }

    public void init(View view){
        listResRCV = view.findViewById(R.id.list_res);
        listResHotRCV = view.findViewById(R.id.list_res_hot);
        listRes = new ArrayList<Restaurant>();
        listResHot = new ArrayList<Restaurant>();
    }
}