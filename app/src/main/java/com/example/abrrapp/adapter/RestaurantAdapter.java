package com.example.abrrapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailResActivity;
import com.example.abrrapp.models.Restaurant;
import com.example.abrrapp.onClick.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder>{
    int layout;
    ArrayList<Restaurant> listRes;
    Context context;

    public RestaurantAdapter(int layout, ArrayList<Restaurant> listRes, Context context) {
        this.layout = layout;
        this.listRes = listRes;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RestaurantAdapter.RestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        Restaurant restaurant = listRes.get(position);
        holder.title.setText(restaurant.getTitle());
        holder.rate.setText("4");
        holder.open.setText(restaurant.getTime_open().substring(0, 5)+ " - "+ restaurant.getTime_close().substring(0, 5));
        holder.address.setText(restaurant.getAddress());
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

    public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title, rate, open, address;
        ItemClickListener itemClickListener;
        public RestaurantHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            rate = itemView.findViewById(R.id.rate);
            open = itemView.findViewById(R.id.open);
            address = itemView.findViewById(R.id.address);
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
