package com.example.productionmanagementandroid.managers;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.R;

public class FooterManager {

    private static final String TAG = "FooterManager";
    private final AppCompatActivity activity;
    private final Button buttonCloseApp;

    public FooterManager(AppCompatActivity activity, View footerView) {
        this.activity = activity;

        buttonCloseApp = footerView.findViewById(R.id.buttonCloseApp);
    }

    public void initialize() {
        setupCloseAppButton();
    }

    private void setupCloseAppButton() {
        if (buttonCloseApp != null) {
            buttonCloseApp.setOnClickListener(v -> closeApp());
        }
    }

    private void closeApp() {
        Log.d(TAG, "closeApp() called");
        activity.finishAffinity();
    }
}