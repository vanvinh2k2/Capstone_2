package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Chatbot;

import java.util.List;

public class ChatbotAdapter extends RecyclerView.Adapter<ChatbotAdapter.ChatbotHolder>{
    Context context;
    int layout;
    List<Chatbot> chatbotList;

    public ChatbotAdapter(Context context, int layout, List<Chatbot> chatbotList) {
        this.context = context;
        this.layout = layout;
        this.chatbotList = chatbotList;
    }

    @NonNull
    @Override
    public ChatbotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ChatbotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatbotHolder holder, int position) {
        Chatbot chatbot = chatbotList.get(position);
        holder.responsetxt.setText(chatbot.getBody_bot());
        if(chatbot.getBody_user().compareTo("chatbodyinit") == 0)
            holder.requesttxt.setVisibility(View.GONE);
        holder.requesttxt.setText(chatbot.getBody_user());
    }

    @Override
    public int getItemCount() {
        return chatbotList.size();
    }

    public class ChatbotHolder extends RecyclerView.ViewHolder{
        TextView requesttxt, responsetxt;
        public ChatbotHolder(@NonNull View itemView) {
            super(itemView);
            requesttxt = itemView.findViewById(R.id.request);
            responsetxt = itemView.findViewById(R.id.response);
        }
    }
}
