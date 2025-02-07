package com.example.productionmanagementandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.productionmanagementandroid.auth.User;
import com.example.productionmanagementandroid.ui.dialogs.LoginDialogFragment;

/**
 * アプリのメインアクティビティ。
 * ログインダイアログの表示、ログイン成功時の画面遷移を行う。
 */
public class MainActivity extends AppCompatActivity implements LoginDialogFragment.LoginSuccessListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "MyPrefs"; // SharedPreferences のファイル名
    private static final String WORKPLACE_ID_KEY = "WorkplacesId"; // SharedPreferences に保存するキー

    /**
     * アクティビティが作成される際に呼び出される。
     *
     * @param savedInstanceState 保存されたインスタンスの状態。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 開始");

        // 画面の端から端までコンテンツを表示するための設定
        EdgeToEdge.enable(this);
        // レイアウトを設定
        setContentView(R.layout.activity_main);
        // システムバーのインセットを処理するためのリスナーを設定
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SharedPreferences から WorkplacesId を取得して Logcat に出力
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedWorkplaceId = sharedPreferences.getInt(WORKPLACE_ID_KEY, -1); // デフォルト値は -1
        Log.d(TAG, "onCreate: SharedPreferences から取得した WorkplacesId: " + savedWorkplaceId);

        Log.d(TAG, "onCreate: アプリ起動 - LoginDialogFragment を表示");

        // ログインダイアログを表示
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.show(getSupportFragmentManager(), "LoginDialog");

        Log.d(TAG, "onCreate: 終了");
    }

    /**
     * ログインが成功した際に呼び出される。
     *
     * @param user ログインしたユーザーの情報。
     * @param selectedWorkplaceId 選択された作業場所のID (null の場合もあり)
     */
    @Override
    public void onLoginSuccess(User user, Integer selectedWorkplaceId) {
        Log.d(TAG, "onLoginSuccess: 開始");
        // ログイン成功時の処理
        Log.d(TAG, "onLoginSuccess: ログイン成功 - MainMenuActivity に遷移");
        Toast.makeText(MainActivity.this, "ログイン成功", Toast.LENGTH_SHORT).show();

        // MainMenuActivity に遷移するための Intent を作成
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        // ユーザーオブジェクトを Intent に追加
        intent.putExtra("user", user);
        // 選択された作業場所のIDをIntentに追加
        intent.putExtra("selectedWorkplaceId", selectedWorkplaceId);
        // MainMenuActivity を開始
        Log.d(TAG, "onLoginSuccess: MainMenuActivity を開始");
        startActivity(intent);
        // MainActivity を終了
        Log.d(TAG, "onLoginSuccess: MainActivity を終了");
        finish();Log.d(TAG, "onLoginSuccess: 終了");
    }
}