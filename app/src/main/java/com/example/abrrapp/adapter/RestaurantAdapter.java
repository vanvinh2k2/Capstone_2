package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Restaurant;
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
        holder.open.setText(restaurant.getTime_open()+ " : "+ restaurant.getTime_close());
        holder.address.setText(restaurant.getAddress());
        Picasso.get().load(restaurant.getImage())
                .placeholder(R.drawable.exres)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listRes.size();
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, rate, open, address;
        public RestaurantHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            rate = itemView.findViewById(R.id.rate);
            open = itemView.findViewById(R.id.open);
            address = itemView.findViewById(R.id.address);
        }
    }
}
