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
import com.example.abrrapp.models.LikeRestaurant;
import com.example.abrrapp.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoveRestaurantAdapter extends RecyclerView.Adapter<LoveRestaurantAdapter.LoveRestaurantHolder>{
    int layout;
    Context context;
    List<LikeRestaurant> listRes;

    public LoveRestaurantAdapter(int layout, Context context, List<LikeRestaurant> listRes) {
        this.layout = layout;
        this.context = context;
        this.listRes = listRes;
    }

    @NonNull
    @Override
    public LoveRestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new LoveRestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoveRestaurantHolder holder, int position) {
        LikeRestaurant likeRestaurant = listRes.get(position);
        holder.titletxt.setText(likeRestaurant.getRestaurant().getTitle());
        holder.ridtxt.setText("Rid: " + likeRestaurant.getRestaurant().getRid());
        holder.contacttxt.setText("Contact: " + likeRestaurant.getRestaurant().getPhone());
        holder.opentxt.setText("Open: " + likeRestaurant.getRestaurant().getTime_open().substring(0, 5)
                + " - " + likeRestaurant.getRestaurant().getTime_close().substring(0, 5));
        Picasso.get().load(likeRestaurant.getRestaurant().getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listRes.size();
    }

    public class LoveRestaurantHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageButton deletebtn;
        TextView titletxt, ridtxt, contacttxt, opentxt;
        public LoveRestaurantHolder(@NonNull View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.title);
            ridtxt = itemView.findViewById(R.id.rid);
            contacttxt = itemView.findViewById(R.id.contact);
            opentxt = itemView.findViewById(R.id.open);
            image = itemView.findViewById(R.id.image);
            deletebtn = itemView.findViewById(R.id.delete);
        }
    }
}
