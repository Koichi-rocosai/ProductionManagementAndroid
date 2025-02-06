package com.example.productionmanagementandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.managers.FooterManager;
import com.example.productionmanagementandroid.managers.HeaderManager;
import com.example.productionmanagementandroid.managers.InputDataManager;

public class ReceiveActivity extends AppCompatActivity {

    private static final String TAG = "ReceiveActivity";
    private HeaderManager headerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_receive);

        // Intentからヘッダー情報を受け取る
        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "Intent is null");
            finish();
            return;
        }
        Stockroom selectedStockroom = intent.getParcelableExtra("selectedStockroom");
        String displayName = intent.getStringExtra("displayName");
        Log.d(TAG, "selectedStockroomName: " + selectedStockroom.getName());
        Log.d(TAG, "displayName: " + displayName);

        // nullチェック
        if (selectedStockroom == null || displayName == null) {
            Log.e(TAG, "selectedStockroom or displayName is null");
            Toast.makeText(this, "必要な情報が不足しています", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ヘッダーの要素を取得
        View headerView = findViewById(R.id.header);
        if (headerView == null) {
            Log.e(TAG, "headerView is null");
            return;
        }

        // ヘッダーマネージャーの初期化
        headerManager = new HeaderManager(this, headerView);
        headerManager.getTextHeaderTitle().setText(R.string.receive_title); // ヘッダーのタイトルを設定
        headerManager.getTextDisplayName().setText(displayName); // 表示名を設定
        headerManager.setSelectedStockroom(selectedStockroom); // 選択された作業場所を設定

        // ログアウトボタンのクリックリスナー
        headerManager.getButtonLogout().setOnClickListener(v -> {
            AuthManager authManager = new AuthManager(this);
            authManager.logout();
            // ログイン画面に戻る
            Intent intentToMain = new Intent(ReceiveActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        });

        // フッターの要素を取得
        View footerView = findViewById(R.id.layoutFooter);
        if (footerView == null) {
            Log.e(TAG, "footerView is null");
            return;
        }

        //フッターマネージャーの初期化
        FooterManager footerManager = new FooterManager(this, footerView);
        footerManager.initialize(); // 初期化処理を呼び出す

        // 製品シリアル番号入力欄の要素を取得
        EditText editTextProductSerial = findViewById(R.id.editTextProductSerial);
        if (editTextProductSerial == null) {
            Log.e(TAG, "editTextProductSerial is null");
        }

        //入力データマネージャーの初期化
        InputDataManager inputDataManager = new InputDataManager(this, editTextProductSerial);
        inputDataManager.initialize(); // 初期化処理を呼び出す
    }
}