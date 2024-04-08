package com.example.abrrapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.abrrapp.R;
import com.example.abrrapp.adapter.DishAdapter;
import com.example.abrrapp.models.Dish;
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
        searchDish();
        return view;
    }

    private void searchDish() {
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchedt.getText().toString().trim();
                List<Dish> dishSearch = new ArrayList<>();
                if(text.length()>0){
                    for(int i=0;i<listDish.size();i++) {
                        if (listDish.get(i).getTitle().toLowerCase().contains(text.toLowerCase())) {
                            dishSearch.add(listDish.get(i));
                        }
                    }
                }else{
                    dishSearch.addAll(listDish);
                }
                dishAdapter.setListDish(dishSearch);
                dishAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteDish(String did){

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