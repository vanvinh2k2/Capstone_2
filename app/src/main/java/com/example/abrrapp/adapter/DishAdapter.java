package com.example.abrrapp.adapter;

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
import com.example.abrrapp.activities.DetailDishActivity;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.onClick.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishHolder>{
    int layout;
    Context context;
    List<Dish> listDish;

    public DishAdapter(int layout, Context context, List<Dish> listDish) {
        this.layout = layout;
        this.context = context;
        this.listDish = listDish;
    }

    @NonNull
    @Override
    public DishHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DishAdapter.DishHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishHolder holder, int position) {
        Dish dish = listDish.get(position);
        holder.byRestxt.setText(dish.getRestaurant().getTitle());
        holder.oldPricetxt.setText(dish.getOld_price()+"$");
        holder.pricetxt.setText(dish.getPrice()+"$");
        Picasso.get().load(dish.getImage()).into(holder.image);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isLongClick) {
                Intent intent = new Intent(context, DetailDishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", dish);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }

    public class DishHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView byRestxt, titletxt, pricetxt, oldPricetxt;
        ImageView image;
        ItemClickListener itemClickListener;
        public DishHolder(@NonNull View itemView) {
            super(itemView);
            byRestxt = itemView.findViewById(R.id.by_res);
            titletxt = itemView.findViewById(R.id.title);
            pricetxt = itemView.findViewById(R.id.price);
            oldPricetxt = itemView.findViewById(R.id.old_price);
            image = itemView.findViewById(R.id.image);
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
