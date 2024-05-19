package com.example.abrrapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailResActivity;
import com.example.abrrapp.models.Rating;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.onClick.ItemClickListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantHotAdapter extends RecyclerView.Adapter<RestaurantHotAdapter.RestaurantHotHolder>{

    int layout;
    ArrayList<Restaurant> listRes;
    ArrayList<Rating> listRate;
    Context context;

    public RestaurantHotAdapter(int layout, ArrayList<Restaurant> listRes, ArrayList<Rating> listRate, Context context) {
        this.layout = layout;
        this.listRes = listRes;
        this.listRate = listRate;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantHotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RestaurantHotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHotHolder holder, int position) {
        Restaurant restaurant = listRes.get(position);
        holder.title.setText(restaurant.getTitle());
        for(int i=0;i< listRate.size();i++) {
            if (listRate.get(i).getRid().compareTo(restaurant.getRid()) == 0) {
                holder.rate.setText(listRate.get(i).getAvg_rating()+"");
            }
        }
        holder.open.setText(restaurant.getTime_open().substring(0, 5)+ " - "+ restaurant.getTime_close().substring(0, 5));
        Picasso.get().load(restaurant.getImage()).into(holder.image);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isLongClick) {
                Intent intent = new Intent(context, DetailResActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", restaurant);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRes.size();
    }

    public class RestaurantHotHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title, rate, open;
        ItemClickListener itemClickListener;
        public RestaurantHotHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.resimg);
            title = itemView.findViewById(R.id.title);
            rate = itemView.findViewById(R.id.rate);
            open = itemView.findViewById(R.id.open);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.setOnItemClick(view, getAdapterPosition(), false);
        }
    }
}
