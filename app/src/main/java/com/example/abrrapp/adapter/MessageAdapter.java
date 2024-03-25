package com.example.abrrapp.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Message;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Message> messageList;
    static final int VIEW_TYPE_SEND = 1;
    static final int VIEW_TYPE_RECEIVED = 0;
    private int image;

    public MessageAdapter(List<Message> messageList, int image) {
        this.messageList = messageList;
        this.image = image;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sender_message,parent,false);
            return new SendMessageHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive_message,parent,false);
            return new ReceiveMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TYPE_SEND) ((SendMessageHolder) holder).setData(messageList.get(position));
        else ((ReceiveMessageHolder) holder).setData(messageList.get(position),image);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getSender().equals("user")) return VIEW_TYPE_SEND;
        else return VIEW_TYPE_RECEIVED;
    }

    class SendMessageHolder extends RecyclerView.ViewHolder{
        TextView timetxt, texttxt;
        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            texttxt = itemView.findViewById(R.id.textMessage);
            timetxt = itemView.findViewById(R.id.textTime);
        }

        void setData(Message chat){
            texttxt.setText(chat.getBody());
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            timetxt.setText(format.format(chat.getDate()));
        }
    }

    class ReceiveMessageHolder extends RecyclerView.ViewHolder{
        TextView timetxt, texttxt;
        ImageView userimg;
        public ReceiveMessageHolder(@NonNull View itemView) {
            super(itemView);
            texttxt = itemView.findViewById(R.id.textMessage);
            timetxt = itemView.findViewById(R.id.textTime);
            userimg = itemView.findViewById(R.id.imageUser);
        }
        void setData(Message chat, int map){
            texttxt.setText(chat.getBody());
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            timetxt.setText(format.format(chat.getDate()));
            userimg.setImageResource(map);
        }
    }
}
