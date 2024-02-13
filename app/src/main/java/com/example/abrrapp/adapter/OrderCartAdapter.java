package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.OrderCartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.OrderCartHolder>{
    int layout;
    Context context;
    List<OrderCartItem> listOrderCartItem;

    public OrderCartAdapter(int layout, Context context, List<OrderCartItem> listOrderCartItem) {
        this.layout = layout;
        this.context = context;
        this.listOrderCartItem = listOrderCartItem;
    }

    @NonNull
    @Override
    public OrderCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new OrderCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCartHolder holder, int position) {
        OrderCartItem orderCartItem = listOrderCartItem.get(position);
        Picasso.get().load(orderCartItem.getDish().getImage()).into(holder.image);
        holder.titletxt.setText(orderCartItem.getDish().getTitle());
        holder.quanlitytxt.setText("Quanlity: " + orderCartItem.getQuantity());
        holder.pricetxt.setText("Price: " + orderCartItem.getDish().getPrice()+"$");
    }

    @Override
    public int getItemCount() {
        return listOrderCartItem.size();
    }

    public class OrderCartHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageButton removeib;
        TextView titletxt, pricetxt, quanlitytxt;
        public OrderCartHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            removeib = itemView.findViewById(R.id.remove);
            titletxt = itemView.findViewById(R.id.title);
            pricetxt = itemView.findViewById(R.id.price);
            quanlitytxt = itemView.findViewById(R.id.quanlity);
        }
    }
}
