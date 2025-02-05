package com.example.productionmanagementandroid.managers;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InputDataManager {
    private static final String TAG = "InputDataManager";
    private final AppCompatActivity activity;
    private final EditText editTextProductSerial;
    private String PRODUCT_SERIAL_NO;
    private String DIVISION_NO;

    public InputDataManager(AppCompatActivity activity, EditText editTextProductSerial) {
        this.activity = activity;
        this.editTextProductSerial = editTextProductSerial;
    }

    public void initialize() {
        setupInputData();
    }

    private void setupInputData() {
        if (editTextProductSerial != null) {
            editTextProductSerial.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String productSerial = editTextProductSerial.getText().toString();
                    if (!productSerial.isEmpty()) {
                        String[] values = productSerial.split(",");
                        if (values.length >= 2) {
                            PRODUCT_SERIAL_NO = values[0];
                            DIVISION_NO = values[1];

                            editTextProductSerial.setText(PRODUCT_SERIAL_NO);

                            Log.d(TAG, "PRODUCT_SERIAL_NO: " + PRODUCT_SERIAL_NO);
                            Log.d(TAG, "DIVISION_NO: " + DIVISION_NO);
                            Toast.makeText(activity, "入力された製品シリアル番号: " + productSerial + " PRODUCT_SERIAL_NO: " + PRODUCT_SERIAL_NO + " DIVISION_NO: " + DIVISION_NO, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "入力された値が不正です: " + productSerial);
                            Toast.makeText(activity, "入力された値が不正です: " + productSerial, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return false;
            });
        }
    }

    public String getProductSerialNo() {
        return PRODUCT_SERIAL_NO;
    }

    public String getDivisionNo() {
        return DIVISION_NO;
    }
}