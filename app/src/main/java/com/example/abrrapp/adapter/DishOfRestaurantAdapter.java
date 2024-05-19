package com.example.abrrapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailResActivity;
import com.example.abrrapp.activities.MenuActivity;
import com.example.abrrapp.models.Dish;
import com.example.abrrapp.models.OrderCartItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DishOfRestaurantAdapter extends RecyclerView.Adapter<DishOfRestaurantAdapter.DishOfRestaurantHolder>{
    int layout;
    MenuActivity context;
    List<Dish> listDish;
    private List<Integer> quantities;

    public DishOfRestaurantAdapter(int layout, MenuActivity context, List<Dish> listDish) {
        this.layout = layout;
        this.context = context;
        this.listDish = listDish;
        this.quantities = new ArrayList<>();
        for (int i = 0; i < listDish.size(); i++) {
            quantities.add(1);
        }
    }

    @NonNull
    @Override
    public DishOfRestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DishOfRestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishOfRestaurantHolder holder, @SuppressLint("RecyclerView") int position) {
        Dish dish = listDish.get(position);
        for(int i=0;i<context.orderCartItems.size();i++){
            if(dish.getDid().compareTo(context.orderCartItems.get(i).getDish().getDid())==0){
                quantities.set(position, context.orderCartItems.get(i).getQuantity());
                holder.addbtn.setVisibility(View.GONE);
                holder.deletebtn.setVisibility(View.VISIBLE);
                break;
            }
        }
        if(dish.isSuggested()){
            holder.suggessed.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(dish.getImage()).into(holder.image);
        holder.quanlitytxt.setText(quantities.get(position).toString());
        holder.pricetxt.setText("Price: "+dish.getPrice()+"$");
        holder.titletxt.setText(dish.getTitle());

        holder.tangtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Gson g = new Gson();
                if(quantities.get(position) <= 15){
                    quantities.set(position, quantities.get(position) + 1);
                    holder.quanlitytxt.setText(quantities.get(position).toString());
                    for(int i=0; i<context.orderCartItems.size();i++){
                        if(dish.getDid().compareTo(context.orderCartItems.get(i).getDish().getDid())==0){
                            context.orderCartItems.get(i).setQuantity(quantities.get(position));
                            //Toast.makeText(context, context.orderCartItems.get(i).getQuantity()+" and "+context.orderCartItems.get(i).getDish().getTitle(), Toast.LENGTH_SHORT).show();
                            //Log.d("onClickData: ", g.toJson(context.convertData()));
                        }
                    }

                }
            }
        });

        holder.giamtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Gson g = new Gson();
                if(quantities.get(position) > 1){
                    quantities.set(position, quantities.get(position) - 1);
                    holder.quanlitytxt.setText(quantities.get(position).toString());
                    for(int i=0; i<context.orderCartItems.size();i++){
                        if(dish.getDid().compareTo(context.orderCartItems.get(i).getDish().getDid())==0){
                            context.orderCartItems.get(i).setQuantity(quantities.get(position));
                        }
                    }
                    //Toast.makeText(context, g.toJson(quantities)+"", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.orderCartItems.add(new OrderCartItem(dish, quantities.get(position)));
                holder.addbtn.setVisibility(View.GONE);
                holder.deletebtn.setVisibility(View.VISIBLE);
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i =0; i<context.orderCartItems.size();i++){
                    if(context.orderCartItems.get(i).getDish().getDid().compareTo(listDish.get(position).getDid())==0){
                        context.orderCartItems.remove(i);
                        break;
                    }
                }
                holder.addbtn.setVisibility(View.VISIBLE);
                holder.deletebtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDish.size();
    }

    public class DishOfRestaurantHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageButton addbtn, deletebtn;
        TextView titletxt, pricetxt, tangtxt, giamtxt, suggessed;
        EditText quanlitytxt;
        public DishOfRestaurantHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            addbtn = itemView.findViewById(R.id.add);
            deletebtn = itemView.findViewById(R.id.delete);
            tangtxt = itemView.findViewById(R.id.tang);
            giamtxt = itemView.findViewById(R.id.giam);
            titletxt = itemView.findViewById(R.id.title);
            pricetxt = itemView.findViewById(R.id.price);
            suggessed = itemView.findViewById(R.id.suggessed);
            quanlitytxt = itemView.findViewById(R.id.quanlity);
        }
    }
}
