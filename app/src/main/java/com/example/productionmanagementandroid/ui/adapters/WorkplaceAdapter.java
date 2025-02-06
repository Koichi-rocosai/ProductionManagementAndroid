package com.example.productionmanagementandroid.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.productionmanagementandroid.auth.Workplace;

import java.util.List;

public class WorkplaceAdapter extends ArrayAdapter<Workplace> {
    public WorkplaceAdapter(@NonNull Context context, @NonNull List<Workplace> workplaces) {
        super(context, android.R.layout.simple_spinner_item, workplaces);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }
}