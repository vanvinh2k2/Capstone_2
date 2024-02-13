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
import com.example.abrrapp.models.Dish;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DishFeatureAdapter extends RecyclerView.Adapter<DishFeatureAdapter.DishFeatureHolder>{
    int layout;
    Context context;
    List<Dish> listDish;

    public DishFeatureAdapter(int layout, Context context, List<Dish> listDish) {
        this.layout = layout;
        this.context = context;
        this.listDish = listDish;
    }

    @NonNull
    @Override
    public DishFeatureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DishFeatureHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishFeatureHolder holder, int position) {
        Dish dish = listDish.get(position);
        Picasso.get().load(dish.getImage()).into(holder.image);
        holder.titletxt.setText(dish.getTitle());
        holder.saletxt.setText("10%");
        holder.pricetxt.setText(dish.getPrice()+"$");
    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }

    public class DishFeatureHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView titletxt, pricetxt, saletxt;
        public DishFeatureHolder(@NonNull View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            pricetxt = itemView.findViewById(R.id.price);
            saletxt = itemView.findViewById(R.id.sale);
        }
    }
}
