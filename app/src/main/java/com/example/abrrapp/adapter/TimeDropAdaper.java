package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailResActivity;
import com.example.abrrapp.models.Table;

import java.util.List;

public class TimeDropAdaper extends BaseAdapter {
    int layout;
    DetailResActivity context;
    List<String> listTime;

    public TimeDropAdaper(int layout, DetailResActivity context, List<String> listTime) {
        this.layout = layout;
        this.context = context;
        this.listTime = listTime;
    }

    @Override
    public int getCount() {
        return listTime.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        TextView seleted = view.findViewById(R.id.seleted);
        seleted.setText(listTime.get(i));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_drop_down,null);
        TextView tvCategory = convertView.findViewById(R.id.item);
        tvCategory.setText(listTime.get(position));
        return convertView;
    }
}
