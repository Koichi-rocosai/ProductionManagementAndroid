package com.example.productionmanagementandroid.managers;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.MainActivity;
import com.example.productionmanagementandroid.R;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;

import java.util.ArrayList;
import java.util.List;

public class HeaderManager {

    private static final String TAG = "HeaderManager";
    private static final String HINT_ITEM = "作業場所を選択";
    private final AppCompatActivity activity;
    private final TextView textDisplayName;
    private final Spinner spinnerStockroom;
    private final TextView textHeaderTitle;
    private final Button buttonLogout;
    private final List<Stockroom> stockrooms;
    private Stockroom selectedStockroom;
    private String displayName;

    public HeaderManager(AppCompatActivity activity, View headerView, List<Stockroom> stockrooms, Stockroom selectedStockroom, String displayName) {
        this.activity = activity;
        this.stockrooms = stockrooms;
        this.selectedStockroom = selectedStockroom;
        this.displayName = displayName;

        textDisplayName = headerView.findViewById(R.id.textDisplayName);
        spinnerStockroom = headerView.findViewById(R.id.spinnerStockroom);
        textHeaderTitle = headerView.findViewById(R.id.textHeaderTitle);
        buttonLogout = headerView.findViewById(R.id.buttonLogout);
    }

    public void initialize() {
        if (textHeaderTitle != null) {
            textHeaderTitle.setText(R.string.receive_title);
        }

        setDisplayName();
        setupSpinner();
        setupLogoutButton();
    }

    private void setDisplayName() {
        if (textDisplayName != null) {
            if (displayName != null) {
                textDisplayName.setText(displayName);
            } else {
                Log.e(TAG, "displayName is null");
                textDisplayName.setText("ユーザー名情報がありません");
            }
        }
    }

    private void setupSpinner() {
        if (spinnerStockroom != null) {
            ArrayAdapter<String> adapter = createSpinnerAdapter(stockrooms);
            spinnerStockroom.setAdapter(adapter);

            int position = adapter.getPosition(selectedStockroom.getName());
            if (position != -1) {
                spinnerStockroom.setSelection(position);
            } else {
                Log.e(TAG, "選択された作業場所名がSpinnerのアダプターに存在しません: " + selectedStockroom.getName());
            }

            spinnerStockroom.setEnabled(true);
            spinnerStockroom.setClickable(true);
            spinnerStockroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        String selectedName = (String) parent.getItemAtPosition(position);
                        selectedStockroom = findStockroomByName(selectedName);
                        if (selectedStockroom != null) {
                            Log.d(TAG, "選択された作業場所: " + selectedStockroom.getName() + ", ID: " + selectedStockroom.getId());
                        } else {
                            Log.e(TAG, "選択された作業場所が見つかりません: " + selectedName);
                        }
                    } else {
                        selectedStockroom = null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedStockroom = null;
                }
            });
        }
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<Stockroom> stockrooms) {
        List<String> stockroomNames = new ArrayList<>();
        stockroomNames.add(HINT_ITEM);
        for (Stockroom stockroom : stockrooms) {
            stockroomNames.add(stockroom.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, stockroomNames) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @NonNull
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setupLogoutButton() {
        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> logout());
        }
    }

    private void logout() {
        AuthManager authManager = new AuthManager(activity);
        authManager.logout();

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private Stockroom findStockroomByName(String name) {
        if (stockrooms != null) {
            for (Stockroom stockroom : stockrooms) {
                if (stockroom.getName().equals(name)) {
                    return stockroom;
                }
            }
        }
        return null;
    }
}