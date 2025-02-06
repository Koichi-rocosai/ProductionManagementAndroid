package com.example.productionmanagementandroid.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.productionmanagementandroid.auth.Stockroom;

import java.util.List;

public class StockroomAdapter extends ArrayAdapter<Stockroom> {
    private static final String HINT_ITEM = "作業場所を選択";

    public StockroomAdapter(Context context, List<Stockroom> stockrooms) {
        super(context, android.R.layout.simple_spinner_item);
        // ヒント用のアイテムを先頭に追加
        Stockroom hintStockroom = new Stockroom(0, HINT_ITEM, null, 0);
        add(hintStockroom);
        addAll(stockrooms);
    }

    @Override
    public boolean isEnabled(int position) {
        // ヒント用のアイテムを選択不可にする
        return position != 0;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            // ヒント用のアイテムのテキストカラーを変更
            tv.setTextColor(Color.GRAY);
        } else {
            tv.setTextColor(Color.BLACK);
        }
        return view;
    }

    @Override
    public Stockroom getItem(int position) {
        return super.getItem(position);
    }
    @Override
    public int getPosition(Stockroom item) {
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < getCount(); i++) {
            Stockroom stockroom = getItem(i);
            if (stockroom != null && stockroom.getId() == item.getId()) {
                return i;
            }
        }
        return -1;
    }
}