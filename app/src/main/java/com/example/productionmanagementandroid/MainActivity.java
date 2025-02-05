package com.example.productionmanagementandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.auth.Stockroom;
import com.example.productionmanagementandroid.auth.StockroomApi;
import com.example.productionmanagementandroid.auth.User;
import com.example.productionmanagementandroid.main.SpinnerAdapterCreator;
import com.example.productionmanagementandroid.ui.dialogs.LoginDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * アプリのメインアクティビティ。
 * ログインダイアログの表示、作業場所の取得、ログイン成功時の画面遷移を行う。
 */
public class MainActivity extends AppCompatActivity implements LoginDialogFragment.LoginSuccessListener {

    private static final String TAG = "MainActivity";

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

        Log.d(TAG, "onCreate: アプリ起動 - LoginDialogFragment を表示");

        // ログインダイアログを表示
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.show(getSupportFragmentManager(), "LoginDialog");

        // 作業場所のリストを取得
        Log.d(TAG, "onCreate: 作業場所のリスト取得を開始");
        fetchStockrooms(loginDialogFragment);
        Log.d(TAG, "onCreate: 終了");
    }

    /**
     * API から作業場所のリストを取得し、ログインダイアログのスピナーに設定する。
     *
     * @param loginDialogFragment ログインダイアログのインスタンス。
     */
    private void fetchStockrooms(LoginDialogFragment loginDialogFragment) {
        Log.d(TAG, "fetchStockrooms: 開始");
        // StockroomApi のインスタンスを作成
        StockroomApi stockroomApi = ApiClient.getClient().create(StockroomApi.class);
        // 作業場所のリストを取得する API リクエストを作成
        Call<List<Stockroom>> call = stockroomApi.getStockrooms();

        // API リクエストを非同期で実行
        call.enqueue(new Callback<>() {
            /**
             * API レスポンスが成功した場合に呼び出される。
             *
             * @param call     API リクエストの Call オブジェクト。
             * @param response API レスポンスの Response オブジェクト。
             */
            @Override
            public void onResponse(@NonNull Call<List<Stockroom>> call, @NonNull Response<List<Stockroom>> response) {
                Log.d(TAG, "fetchStockrooms: onResponse: 開始");
                // レスポンスが成功し、かつボディが null でない場合
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "fetchStockrooms: onResponse: APIからのデータ取得に成功");
                    // レスポンスボディから作業場所のリストを取得
                    List<Stockroom> stockrooms = response.body();
                    // スピナー用のアダプターを作成
                    Log.d(TAG, "fetchStockrooms: onResponse: スピナー用アダプター作成を開始");
                    SpinnerAdapterCreator adapterCreator = new SpinnerAdapterCreator(MainActivity.this);
                    ArrayAdapter<String> adapter = adapterCreator.createSpinnerAdapter(stockrooms);
                    Log.d(TAG, "fetchStockrooms: onResponse: スピナー用アダプター作成に成功");
                    // ログインダイアログのスピナーにアダプターを設定
                    Log.d(TAG, "fetchStockrooms: onResponse: ログインダイアログのスピナーにアダプターを設定");
                    loginDialogFragment.setSpinnerAdapter(adapter);
                    Log.d(TAG, "fetchStockrooms: onResponse: ログインダイアログのスピナーにアダプターを設定に成功");
                } else {
                    // API からのデータ取得に失敗した場合
                    Log.e(TAG, "fetchStockrooms: onResponse: APIからのデータ取得に失敗: " + response.message());
                    Toast.makeText(MainActivity.this, "APIからのデータ取得に失敗", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "fetchStockrooms: onResponse: 終了");
            }

            /**
             * API リクエストが失敗した場合に呼び出される。
             *
             * @param call API リクエストの Call オブジェクト。
             * @param t    API リクエストが失敗した理由を表す Throwable オブジェクト。
             */
            @Override
            public void onFailure(@NonNull Call<List<Stockroom>> call, @NonNull Throwable t) {
                Log.e(TAG, "fetchStockrooms: onFailure: 通信エラー: " + t.getMessage());
                Toast.makeText(MainActivity.this, "通信エラー", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "fetchStockrooms: 終了");
    }

    /**
     * ログインが成功した際に呼び出される。
     *
     * @param user                  ログインしたユーザーの情報。
     * @param selectedStockroomName 選択された作業場所の名前。
     */
    @Override
    public void onLoginSuccess(User user, String selectedStockroomName) {
        Log.d(TAG, "onLoginSuccess: 開始");
        // ログイン成功時の処理
        Log.d(TAG, "onLoginSuccess: ログイン成功 - MainMenuActivity に遷移");
        Toast.makeText(MainActivity.this, "ログイン成功", Toast.LENGTH_SHORT).show();

        // MainMenuActivity に遷移するための Intent を作成
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        // 選択された作業場所の名前を Intent に追加
        intent.putExtra("selectedStockroomName", selectedStockroomName);
        // ユーザーオブジェクトを Intent に追加
        intent.putExtra("user", user);
        // MainMenuActivity を開始
        Log.d(TAG, "onLoginSuccess: MainMenuActivity を開始");
        startActivity(intent);
        // MainActivity を終了
        Log.d(TAG, "onLoginSuccess: MainActivity を終了");
        finish();
        Log.d(TAG, "onLoginSuccess: 終了");
    }
}