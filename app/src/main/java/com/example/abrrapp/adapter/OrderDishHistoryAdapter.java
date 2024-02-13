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
import com.example.abrrapp.models.Order;
import com.example.abrrapp.models.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDishHistoryAdapter extends RecyclerView.Adapter<OrderDishHistoryAdapter.OrderDishHistoryHolder>{
    int layout;
    Context context;
    List<OrderItem> listOrderItem;

    public OrderDishHistoryAdapter(int layout, Context context, List<OrderItem> listOrderItem) {
        this.layout = layout;
        this.context = context;
        this.listOrderItem = listOrderItem;
    }

    @NonNull
    @Override
    public OrderDishHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new OrderDishHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDishHistoryHolder holder, int position) {
        OrderItem orderItem = listOrderItem.get(position);
        Picasso.get().load(orderItem.getDish().getImage()).into(holder.image);
        holder.titletxt.setText(orderItem.getDish().getTitle());
        holder.categorytxt.setText("Category: "+orderItem.getDish().getCategory().getTitle());
        holder.quanlitytxt.setText("X"+orderItem.getQuantity());
        holder.pricetxt.setText("Price: "+orderItem.getTotal()+"$");
    }

    @Override
    public int getItemCount() {
        return listOrderItem.size();
    }

    public class OrderDishHistoryHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView titletxt, categorytxt, pricetxt, quanlitytxt;
        public OrderDishHistoryHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            titletxt = itemView.findViewById(R.id.title);
            categorytxt = itemView.findViewById(R.id.category);
            pricetxt = itemView.findViewById(R.id.price);
            quanlitytxt = itemView.findViewById(R.id.quanlity);
        }
    }
}
