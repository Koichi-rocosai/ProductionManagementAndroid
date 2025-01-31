package com.example.productionmanagementandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.auth.StockroomApi;
import com.example.productionmanagementandroid.ui.dialogs.LoginDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String HINT_ITEM = "作業場所を選択"; // ヒント用のアイテム
    private AuthManager authManager;
    private String selectedStockroomNameFromLogin = null; // LoginDialogFragmentから受け取った作業場所の名前を保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authManager = new AuthManager(this);

        Log.d(TAG, "アプリ起動 - LoginDialogFragment を表示");

        // アプリ起動時にログインダイアログを表示
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.show(getSupportFragmentManager(), "LoginDialog");

        // StockroomApi を使用して API からデータを取得し、spinnerLoginType にリストを格納する処理
        fetchStockrooms(loginDialogFragment);

        // LoginDialogFragment のコールバックを設定
        loginDialogFragment.setLoginDialogListener((username, password, selectedStockroomName) -> {
            // ログイン処理を実行
            authManager.login(username, password, new AuthManager.LoginCallback() {
                @Override
                public void onSuccess() {
                    // ログイン成功時の処理
                    Log.d(TAG, "ログイン成功 - MainMenuActivity に遷移");
                    Toast.makeText(MainActivity.this, "ログイン成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                    intent.putExtra("selectedStockroomName", selectedStockroomName); // 作業場所の情報を渡す
                    startActivity(intent);
                    finish(); // MainActivity を終了
                }

                @Override
                public void onFailure(String message) {
                    // ログイン失敗時の処理
                    Log.e(TAG, "ログイン失敗: " + message);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void fetchStockrooms(LoginDialogFragment loginDialogFragment) {
        StockroomApi stockroomApi = ApiClient.getClient().create(StockroomApi.class);
        Call<List<Stockroom>> call = stockroomApi.getStockrooms();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Stockroom>> call, @NonNull Response<List<Stockroom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Stockroom> stockrooms = response.body();
                    ArrayAdapter<String> adapter = createSpinnerAdapter(stockrooms);
                    loginDialogFragment.setSpinnerAdapter(adapter);
                } else {
                    Log.e(TAG, "APIからのデータ取得に失敗: " + response.message());
                    Toast.makeText(MainActivity.this, "APIからのデータ取得に失敗", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stockroom>> call, @NonNull Throwable t) {
                Log.e(TAG, "通信エラー: " + t.getMessage());
                Toast.makeText(MainActivity.this, "通信エラー", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<Stockroom> stockrooms) {
        List<String> stockroomNames = new ArrayList<>();
        stockroomNames.add(HINT_ITEM); // ヒント用のアイテムを先頭に追加
        for (Stockroom stockroom : stockrooms) {
            stockroomNames.add(stockroom.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, stockroomNames) {
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