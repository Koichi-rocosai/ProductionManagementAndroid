package com.example.productionmanagementandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;

import java.util.ArrayList;
import java.util.List;

public class ReceiveActivity extends AppCompatActivity {

    private static final String TAG = "ReceiveActivity";
    private Stockroom selectedStockroom; // 選択されたStockroomオブジェクトを保持
    private String displayName;
    private Spinner spinnerStockroom; // ヘッダーのSpinner
    private List<Stockroom> stockrooms; // Stockroomのリストを保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_receive);

        // Intentからヘッダー情報を受け取る
        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "Intent is null");
            finish(); // Intentがnullの場合はActivityを終了
            return;
        }
        selectedStockroom = intent.getParcelableExtra("selectedStockroom"); // Stockroomオブジェクトを受け取る
        displayName = intent.getStringExtra("displayName");
        stockrooms = intent.getParcelableArrayListExtra("stockrooms"); // Stockroomのリストを受け取る
        Log.d(TAG, "selectedStockroomName: " + selectedStockroom.getName());
        Log.d(TAG, "displayName: " + displayName);

        // nullチェック
        if (selectedStockroom == null || displayName == null || stockrooms == null) {
            Log.e(TAG, "selectedStockroom or displayName or stockrooms is null");
            Toast.makeText(this, "必要な情報が不足しています", Toast.LENGTH_SHORT).show();
            finish(); // 必要な情報が不足している場合はActivityを終了
            return;
        }

        // ヘッダーの要素を取得
        View headerView = findViewById(R.id.header); // activity_receive.xml 内の header を取得
        if (headerView == null) {
            Log.e(TAG, "headerView is null");
            return;
        }

        // header.xml 内の View を取得
        TextView textDisplayName = headerView.findViewById(R.id.textDisplayName);
        spinnerStockroom = headerView.findViewById(R.id.spinnerStockroom);
        TextView textHeaderTitle = headerView.findViewById(R.id.textHeaderTitle);
        Button buttonLogout = headerView.findViewById(R.id.buttonLogout); // ログアウトボタンを取得

        // nullチェック
        if (textDisplayName == null) {
            Log.e(TAG, "textDisplayName is null");
        }
        if (spinnerStockroom == null) {
            Log.e(TAG, "spinnerStockroom is null");
        }
        if (textHeaderTitle == null) {
            Log.e(TAG, "textHeaderTitle is null");
        }
        if (buttonLogout == null) {
            Log.e(TAG, "buttonLogout is null");
        }

        if (textHeaderTitle != null) {
            textHeaderTitle.setText(R.string.receive_title); // ヘッダーのタイトルを設定
        }

        // ユーザー名を表示
        if (textDisplayName != null) {
            setDisplayName(textDisplayName);
        }

        // Spinnerに選択された作業場所名を表示
        if (spinnerStockroom != null && selectedStockroom != null) {
            // Spinnerに表示するデータを設定
            List<String> stockroomNames = getStockroomNames(stockrooms);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stockroomNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerStockroom.setAdapter(adapter);

            // Spinnerの選択を強制的に設定
            int position = adapter.getPosition(selectedStockroom.getName());
            if (position != -1) {
                spinnerStockroom.setSelection(position);
            } else {
                Log.e(TAG, "選択された作業場所名がSpinnerのアダプターに存在しません: " + selectedStockroom.getName());
            }
            // Spinnerを操作不可にする
            // Spinnerのクリックイベントを無効化
            spinnerStockroom.setEnabled(false);
            spinnerStockroom.setClickable(false);
        }
        // ログアウトボタンのクリックイベントを設定
        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> {
                // ログアウト処理を実装
                logout();
            });
        }

        // フッターの要素を取得
        View footerView = findViewById(R.id.layoutFooter); // activity_receive.xml 内の footer を取得
        if (footerView == null) {
            Log.e(TAG, "footerView is null");
            return;
        }
        Button buttonCloseApp = footerView.findViewById(R.id.buttonCloseApp); // アプリを閉じるボタンを取得
        if (buttonCloseApp == null) {
            Log.e(TAG, "buttonCloseApp is null");
            return;
        }
        // アプリを閉じるボタンのクリックイベントを設定
        buttonCloseApp.setOnClickListener(v -> {
            // アプリを完全に閉じる処理を実装
            closeApp();
        });
    }

    private void setDisplayName(TextView textDisplayName) {
        if (displayName != null) {
            textDisplayName.setText(displayName);
        } else {
            Log.e(TAG, "displayName is null");
            textDisplayName.setText("ユーザー名情報がありません");
        }
    }

    // Stockroomのリストから作業場所名のリストを取得するメソッド
    private List<String> getStockroomNames(List<Stockroom> stockrooms) {
        List<String> stockroomNames = new ArrayList<>();
        for (Stockroom stockroom : stockrooms) {
            stockroomNames.add(stockroom.getName());
        }
        return stockroomNames;
    }

    // ログアウト処理
    private void logout() {
        // AuthManagerを使用してログアウト処理を行う
        AuthManager authManager = new AuthManager(this);
        authManager.logout();

        // ログイン画面に遷移
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // アプリを完全に閉じる処理
    private void closeApp() {
        finishAffinity(); // アプリを完全に閉じる
        // オプション: アプリを完全に閉じたことをシステムに通知する
        // System.exit(0);
    }
}