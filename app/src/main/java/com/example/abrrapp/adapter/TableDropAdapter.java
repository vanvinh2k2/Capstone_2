package com.example.abrrapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abrrapp.R;
import com.example.abrrapp.activities.DetailResActivity;
import com.example.abrrapp.activities.MenuActivity;
import com.example.abrrapp.models.Table;

import java.util.List;

public class TableDropAdapter extends BaseAdapter {
    int layout;
    MenuActivity context;
    List<Table> listTable;

    public TableDropAdapter(int layout, MenuActivity context, List<Table> listRes) {
        this.layout = layout;
        this.context = context;
        this.listTable = listRes;
    }

    @Override
    public int getCount() {
        return listTable.size();
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
        context.idTable = listTable.get(i).getTid();
        if(i != 0) seleted.setText(listTable.get(i).getTitle()
                + " ("+ listTable.get(i).getNumber_seat() + " peoples)");
        else seleted.setText(listTable.get(i).getTitle());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_drop_down,null);
        TextView tvCategory = convertView.findViewById(R.id.item);
        if(position != 0) tvCategory.setText(listTable.get(position).getTitle()
                        + " ("+ listTable.get(position).getNumber_seat() + " peoples)");
        else tvCategory.setText(listTable.get(position).getTitle());
        context.idTable = listTable.get(position).getTid();
        return convertView;
    }
}
