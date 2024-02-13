package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abrrapp.R;
import com.example.abrrapp.models.Category;
import com.example.abrrapp.models.Table;

import java.util.List;

public class CategoryDropAdapter extends BaseAdapter {
    int layout;
    Context context;
    List<Category> listCategory;

    public CategoryDropAdapter(int layout, Context context, List<Category> listCategory) {
        this.layout = layout;
        this.context = context;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
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
        seleted.setText(listCategory.get(i).getTitle());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_drop_down,null);
        TextView tvCategory = convertView.findViewById(R.id.item);
        tvCategory.setText(listCategory.get(position).getTitle());
        return convertView;
    }
}
