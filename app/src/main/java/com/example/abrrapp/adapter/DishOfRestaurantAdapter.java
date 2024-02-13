package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Dish;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DishOfRestaurantAdapter extends RecyclerView.Adapter<DishOfRestaurantAdapter.DishOfRestaurantHolder>{
    int layout;
    Context context;
    List<Dish> listDish;

    public DishOfRestaurantAdapter(int layout, Context context, List<Dish> listDish) {
        this.layout = layout;
        this.context = context;
        this.listDish = listDish;
    }

    @NonNull
    @Override
    public DishOfRestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DishOfRestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishOfRestaurantHolder holder, int position) {
        Dish dish = listDish.get(position);
        Picasso.get().load(dish.getImage()).into(holder.image);
        holder.quanlitytxt.setText("1");
        holder.pricetxt.setText("Price: "+dish.getPrice()+"$");
        holder.titletxt.setText(dish.getTitle());
    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }

    public class DishOfRestaurantHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageButton addbtn;
        TextView titletxt, pricetxt, tangtxt, giamtxt;
        EditText quanlitytxt;
        public DishOfRestaurantHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            addbtn = itemView.findViewById(R.id.add);
            tangtxt = itemView.findViewById(R.id.tang);
            giamtxt = itemView.findViewById(R.id.giam);
            titletxt = itemView.findViewById(R.id.title);
            pricetxt = itemView.findViewById(R.id.price);
            quanlitytxt = itemView.findViewById(R.id.quanlity);
        }
    }
}
