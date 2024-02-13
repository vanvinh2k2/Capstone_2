package com.example.abrrapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailHistoryOrderActivity;
import com.example.abrrapp.models.Order;
import com.example.abrrapp.onClick.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder>{
    int layout;
    Context context;
    List<Order> listOrder;
    HashMap<String, String> arr = new HashMap<>();

    public OrderHistoryAdapter(int layout, Context context, List<Order> listOrder) {
        this.layout = layout;
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        arr.put("awaiting_confirmation", "Awaiting confirmation");
        arr.put("confirmed", "Confirmed");
        arr.put("complete", "Complete");
        arr.put("cancel", "Cancel");
        Order order = listOrder.get(position);
        holder.oidtxt.setText("OID: "+ order.getOid());
        holder.titletxt.setText(order.getRestaurant().getTitle());
        holder.statustxt.setText(arr.get(order.getProduct_status()));
        holder.datetxt.setText("Date order: " + order.getOrder_date());
        holder.numPeopletxt.setText("Number people: " + order.getNumber_people()+"");
        holder.totaltxt.setText("Total: " + order.getPrice()+"$");
        Picasso.get().load(order.getRestaurant().getImage()).into(holder.image);

        if(order.getProduct_status().compareTo("confirmed") == 0) {
            holder.statustxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }
        if(order.getProduct_status().compareTo("complete") == 0){
            holder.statustxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blu));
        }
        if(order.getProduct_status().compareTo("cancel") == 0){
            holder.statustxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isLongClick) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", order);
                Intent intent = new Intent(context, DetailHistoryOrderActivity.class);
                intent.putExtra("bundle", bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView oidtxt, statustxt, titletxt, datetxt, numPeopletxt, totaltxt;
        ItemClickListener clickListener;
        ImageView image;
        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            oidtxt = itemView.findViewById(R.id.oid);
            statustxt = itemView.findViewById(R.id.status);
            titletxt = itemView.findViewById(R.id.title);
            datetxt = itemView.findViewById(R.id.date);
            numPeopletxt = itemView.findViewById(R.id.num_people);
            totaltxt = itemView.findViewById(R.id.total);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.setOnItemClick(view, getAdapterPosition(), false);
        }
    }
}
