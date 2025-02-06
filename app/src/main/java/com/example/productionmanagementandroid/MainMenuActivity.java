package com.example.productionmanagementandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.auth.StockroomApi;
import com.example.productionmanagementandroid.managers.HeaderManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";
    private String displayName = null; // 表示名を保持
    private Button buttonReceive; // buttonReceiveをクラス変数として追加
    private HeaderManager headerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // ヘッダーの要素を取得
        View headerView = findViewById(R.id.header); // header.xmlをincludeしたViewを取得
        //HeaderManagerのインスタンスを作成
        headerManager = new HeaderManager(this, headerView);
        headerManager.initialize();
        headerManager.getTextHeaderTitle().setText(R.string.main_menu_title); // ヘッダーのタイトルを設定

        // メインフィールドのボタンを取得
        buttonReceive = findViewById(R.id.buttonReceive);
        Button buttonMove = findViewById(R.id.buttonMove);
        Button buttonPrepare = findViewById(R.id.buttonPrepare);
        Button buttonShip = findViewById(R.id.buttonShip);

        // フッターのボタンを取得
        Button buttonCloseApp = findViewById(R.id.buttonCloseApp);

        // ユーザー名を表示
        AuthManager authManager = new AuthManager(this);
        displayName = authManager.getDisplayName();
        headerManager.getTextDisplayName().setText(displayName);

        // ログアウトボタンのクリックリスナー
        headerManager.getButtonLogout().setOnClickListener(v -> {
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
        buttonReceive.setEnabled(false); // 初期状態では無効化
        fetchStockrooms();

        // 各ボタンのクリックリスナーを設定
        buttonReceive.setOnClickListener(v -> {
            // 入庫処理の画面に遷移する処理を記述
            Stockroom selectedStockroom = headerManager.getSelectedStockroom();
            if (selectedStockroom != null) {
                Intent intentToReceive = new Intent(MainMenuActivity.this, ReceiveActivity.class);
                intentToReceive.putExtra("selectedStockroom", selectedStockroom); // Stockroomオブジェクトを渡す
                intentToReceive.putExtra("displayName", displayName); // 表示名の情報を渡す
                //intentToReceive.putParcelableArrayListExtra("stockrooms", new ArrayList<>(stockrooms)); // Stockroomのリストを渡す
                startActivity(intentToReceive);
            } else {
                Toast.makeText(this, "作業場所が選択されていません", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "selectedStockroom is null");
            }
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

    private void fetchStockrooms() {
        StockroomApi stockroomApi = ApiClient.getClient().create(StockroomApi.class);
        Call<List<Stockroom>> call = stockroomApi.getStockrooms();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Stockroom>> call, @NonNull Response<List<Stockroom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Stockroom> stockrooms = response.body(); // APIから取得したStockroomのリストを保持
                    headerManager.setStockrooms(stockrooms);
                    buttonReceive.setEnabled(true); // API通信が完了したら有効化
                } else {
                    Log.e(TAG, "APIからのデータ取得に失敗: " + response.message());
                    Toast.makeText(MainMenuActivity.this, "APIからのデータ取得に失敗", Toast.LENGTH_SHORT).show();
                    buttonReceive.setEnabled(true); // API通信が失敗しても有効化
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Stockroom>> call, @NonNull Throwable t) {
                Log.e(TAG, "通信エラー: " + t.getMessage());
                Toast.makeText(MainMenuActivity.this, "通信エラー", Toast.LENGTH_SHORT).show();
                buttonReceive.setEnabled(true); // API通信が失敗しても有効化
            }
        });
    }
}