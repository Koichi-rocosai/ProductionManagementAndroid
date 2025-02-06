package com.example.productionmanagementandroid.managers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.productionmanagementandroid.R;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.ui.adapters.StockroomAdapter;

import java.util.List;

public class HeaderManager {

    private static final String TAG = "HeaderManager";
    private final Context context;
    private final TextView textDisplayName;
    private final Spinner spinnerStockroom;
    private final TextView textHeaderTitle;
    private final Button buttonLogout;
    private Stockroom selectedStockroom;

    public HeaderManager(Context context, View headerView) {
        this.context = context;

        textDisplayName = headerView.findViewById(R.id.textDisplayName);
        spinnerStockroom = headerView.findViewById(R.id.spinnerStockroom);
        textHeaderTitle = headerView.findViewById(R.id.textHeaderTitle);
        buttonLogout = headerView.findViewById(R.id.buttonLogout);
    }

    public void initialize() {
        Log.d(TAG, "initialize: 開始");
        Log.d(TAG, "initialize: 終了");
    }

    public void setStockrooms(List<Stockroom> stockrooms) {
        Log.d(TAG, "setStockrooms: 開始");
        // StockroomAdapter を作成
        StockroomAdapter adapter = new StockroomAdapter(context, stockrooms);
        Log.d(TAG, "setStockrooms: StockroomAdapter 作成");

        // Spinner に Adapter を設定
        spinnerStockroom.setAdapter(adapter);
        Log.d(TAG, "setStockrooms: Spinner に Adapter を設定");

        // Spinner の初期選択を設定
        if (selectedStockroom != null) {
            int selectionPosition = adapter.getPosition(selectedStockroom);
            spinnerStockroom.setSelection(selectionPosition);
            Log.d(TAG, "setStockrooms: Spinner の初期選択を設定 - 選択されたStockroom: " + selectedStockroom.getName());
        }

        // Spinner の選択リスナーを設定
        spinnerStockroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStockroom = (Stockroom) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: Stockroom が選択されました - 選択されたStockroom: " + selectedStockroom.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStockroom = null;
                Log.d(TAG, "onNothingSelected: Stockroom が選択されていません");
            }
        });
        Log.d(TAG, "setStockrooms: 終了");
    }

    public Stockroom getSelectedStockroom() {
        return selectedStockroom;
    }

    public void setSelectedStockroom(Stockroom selectedStockroom) {
        this.selectedStockroom = selectedStockroom;
    }
    public TextView getTextDisplayName() {
        return textDisplayName;
    }
    public Button getButtonLogout() {
        return buttonLogout;
    }
    public TextView getTextHeaderTitle() {
        return textHeaderTitle;
    }
}