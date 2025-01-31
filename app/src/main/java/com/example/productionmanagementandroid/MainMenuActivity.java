package com.example.productionmanagementandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.auth.StockroomApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";
    private static final String HINT_ITEM = "作業場所を選択"; // ヒント用のアイテム
    private String selectedStockroomNameFromLogin = null; // MainActivityから受け取った作業場所の名前を保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Intentから作業場所の名前を受け取る
        Intent intent = getIntent();
        selectedStockroomNameFromLogin = intent.getStringExtra("selectedStockroomName");
        Log.d(TAG, "受け取った作業場所名: " + selectedStockroomNameFromLogin);

        // ヘッダーの要素を取得
        TextView textDisplayName = findViewById(R.id.textDisplayName);
        Spinner spinnerStockroom = findViewById(R.id.spinnerStockroom);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        // メインフィールドのボタンを取得
        Button buttonReceive = findViewById(R.id.buttonReceive);
        Button buttonMove = findViewById(R.id.buttonMove);
        Button buttonPrepare = findViewById(R.id.buttonPrepare);
        Button buttonShip = findViewById(R.id.buttonShip);

        // フッターのボタンを取得
        Button buttonCloseApp = findViewById(R.id.buttonCloseApp);

        // ユーザー名を表示
        AuthManager authManager = new AuthManager(this);
        String displayName = authManager.getDisplayName();
        textDisplayName.setText(displayName);

        // ログアウトボタンのクリックリスナー
        buttonLogout.setOnClickListener(v -> {
            authManager.logout();
            // ログイン画面に戻る
            Intent intentToMain = new Intent(MainMenuActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        });

        // アプリを閉じるボタンのクリックリスナー
        buttonCloseApp.setOnClickListener(v -> {
            finishAffinity(); // アプリを完全に閉じる
        });

        // StockroomApi を使用して API からデータを取得し、spinnerStockroom にリストを格納する処理
        fetchStockrooms(spinnerStockroom);

        // 各ボタンのクリックリスナーを設定（仮実装）
        buttonReceive.setOnClickListener(v -> {
            Toast.makeText(this, "入庫処理ボタンがクリックされました", Toast.LENGTH_SHORT).show();
            // 入庫処理の画面に遷移する処理を記述
        });

        buttonMove.setOnClickListener(v -> {
            Toast.makeText(this, "在庫移動ボタンがクリックされました", Toast.LENGTH_SHORT).show();
            // 在庫移動の画面に遷移する処理を記述
        });

        buttonPrepare.setOnClickListener(v -> {
            Toast.makeText(this, "束線準備処理ボタンがクリックされました", Toast.LENGTH_SHORT).show();
            // 束線準備処理の画面に遷移する処理を記述
        });

        buttonShip.setOnClickListener(v -> {
            Toast.makeText(this, "出庫処理ボタンがクリックされました", Toast.LENGTH_SHORT).show();
            // 出庫処理の画面に遷移する処理を記述
        });
    }

    private void fetchStockrooms(Spinner spinnerStockroom) {
        StockroomApi stockroomApi = ApiClient.getClient().create(StockroomApi.class);
        Call<List<Stockroom>> call = stockroomApi.getStockrooms();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Stockroom>> call, @NonNull Response<List<Stockroom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Stockroom> stockrooms = response.body();
                    ArrayAdapter<String> adapter = createSpinnerAdapter(stockrooms);
                    spinnerStockroom.setAdapter(adapter);

                    // 初期選択を設定
                    if (selectedStockroomNameFromLogin != null) {
                        int position = adapter.getPosition(selectedStockroomNameFromLogin);
                        if (position != -1) {
                            spinnerStockroom.setSelection(position);
                        }
                    }
                } else {
                    Log.e(TAG, "APIからのデータ取得に失敗: " + response.message());
                    Toast.makeText(MainMenuActivity.this, "APIからのデータ取得に失敗", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stockroom>> call, @NonNull Throwable t) {
                Log.e(TAG, "通信エラー: " + t.getMessage());
                Toast.makeText(MainMenuActivity.this, "通信エラー", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<Stockroom> stockrooms) {
        List<String> stockroomNames = new ArrayList<>();
        stockroomNames.add(HINT_ITEM); // ヒント用のアイテムを先頭に追加
        for (Stockroom stockroom : stockrooms) {
            stockroomNames.add(stockroom.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainMenuActivity.this, android.R.layout.simple_spinner_item, stockroomNames) {
            @Override
            public boolean isEnabled(int position) {
// ヒント用のアイテムを選択不可にする
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // ヒント用のアイテムのテキストカラーを変更
                    tv.setTextColor(Color.GRAY);
                } else {
                    // ヒント以外のアイテムのテキストカラーを黒に変更
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}