package com.example.productionmanagementandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";
    private static final String HINT_ITEM = "倉庫名を選択"; // ヒント用のアイテム
    private static final String PREFS_NAME = "MyPrefs"; // SharedPreferences のファイル名
    private static final String WORKPLACE_ID_KEY = "WorkplacesId"; // SharedPreferences に保存するキー
    private String selectedStockroomName = null; // 選択された作業場所の名前を保持
    private String displayName = null; // 表示名を保持
    private Spinner spinnerStockroom;
    private Button buttonReceive; // buttonReceiveをクラス変数として追加
    private List<Stockroom> stockrooms; // APIから取得したStockroomのリストを保持
    private Stockroom selectedStockroom; // 選択されたStockroomオブジェクトを保持
    private String outsourcingId; // OutsourcingIdを保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // SharedPreferences から WorkplacesId を取得して Logcat に出力
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //savedWorkplaceId = sharedPreferences.getInt(WORKPLACE_ID_KEY, -1); // デフォルト値は -1 //削除
        //Log.d(TAG, "onCreate: SharedPreferences から取得した WorkplacesId: " + savedWorkplaceId); //削除

        // Intentから作業場所の名前を受け取る
        Intent intent = getIntent();
        selectedStockroomName = intent.getStringExtra("selectedStockroomName");
        Log.d(TAG, "受け取った倉庫名: " + selectedStockroomName);

        // IntentからselectedWorkplaceIdを受け取る
        //Integer selectedWorkplaceId = intent.getIntExtra("selectedWorkplaceId", -1); //削除
        //Log.d(TAG, "onCreate: Intentから受け取ったselectedWorkplaceId: " + selectedWorkplaceId); //削除

        // ヘッダーの要素を取得
        View headerView = findViewById(R.id.header); // header.xmlをincludeしたViewを取得
        TextView textDisplayName = headerView.findViewById(R.id.textDisplayName); // header.xml内のTextViewを取得
        spinnerStockroom = headerView.findViewById(R.id.spinnerStockroom); // header.xml内のSpinnerを取得
        Button buttonLogout = headerView.findViewById(R.id.buttonLogout); // header.xml内のButtonを取得
        TextView textHeaderTitle = headerView.findViewById(R.id.textHeaderTitle); // header.xml内のTextViewを取得
        textHeaderTitle.setText(R.string.main_menu_title); // ヘッダーのタイトルを設定

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
        outsourcingId = authManager.getOutsourcingId(); // OutsourcingIdを取得
        setDisplayName(textDisplayName);

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
        buttonReceive.setEnabled(false); // 初期状態では無効化
        fetchStockrooms();

        // 各ボタンのクリックリスナーを設定
        buttonReceive.setOnClickListener(v -> {
            // 入庫処理の画面に遷移する処理を記述
            if (selectedStockroom != null) {
                Intent intentToReceive = new Intent(MainMenuActivity.this, ReceiveActivity.class);
                intentToReceive.putExtra("selectedStockroom", selectedStockroom); // Stockroomオブジェクトを渡す
                intentToReceive.putExtra("displayName", displayName); // 表示名の情報を渡す
                intentToReceive.putParcelableArrayListExtra("stockrooms", new ArrayList<>(stockrooms)); // Stockroomのリストを渡す
                startActivity(intentToReceive);
            } else {
                Toast.makeText(this, "倉庫名が選択されていません", Toast.LENGTH_SHORT).show();
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

        // Spinnerのアイテム選択時の処理
        spinnerStockroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // ヒントアイテム以外が選択された場合
                    String selectedName = (String) parent.getItemAtPosition(position);
                    selectedStockroom = findStockroomByName(selectedName);
                    if (selectedStockroom != null) {
                        Log.d(TAG, "選択された倉庫名: " + selectedStockroom.getName() + ", ID: " + selectedStockroom.getId());
                    } else {
                        Log.e(TAG, "選択された倉庫名が見つかりません: " + selectedName);
                    }
                } else {
                    selectedStockroom = null; // ヒントアイテムが選択された場合はnullにする
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStockroom = null;
            }
        });
    }

    private void fetchStockrooms() {
        StockroomApi stockroomApi = ApiClient.getClient().create(StockroomApi.class);
        Call<List<Stockroom>> call = stockroomApi.getStockrooms();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Stockroom>> call, @NonNull Response<List<Stockroom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stockrooms = response.body(); // APIから取得したStockroomのリストを保持
                    ArrayAdapter<String> adapter = createSpinnerAdapter(stockrooms);
                    spinnerStockroom.setAdapter(adapter);
                    setSpinnerStockroom(adapter);
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

    private ArrayAdapter<String> createSpinnerAdapter(List<Stockroom> stockrooms) {
        List<String> stockroomNames = new ArrayList<>();
        stockroomNames.add(HINT_ITEM); // ヒント用のアイテムを先頭に追加

        List<Stockroom> filteredStockrooms;
        // OutsourcingId がブランクでない場合のみフィルターを適用
        if (!outsourcingId.isEmpty()) {
            filteredStockrooms = stockrooms.stream()
                    .filter(stockroom -> {
                        // stockroom.getOutsourcingId() が null の場合を考慮
                        if (stockroom.getOutsourcingId() == null) {
                            return false;
                        }
                        return stockroom.getOutsourcingId().equals(outsourcingId);
                    })
                    .collect(Collectors.toList());
        } else {
            filteredStockrooms = stockrooms;
        }

        for (Stockroom stockroom : filteredStockrooms) {
            stockroomNames.add(stockroom.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainMenuActivity.this, android.R.layout.simple_spinner_item, stockroomNames) {
            @Override
            public boolean isEnabled(int position) {
                // ヒント用のアイテムを選択不可にする
                return position != 0;
            }

            @NonNull
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // ヒント用のアイテムのテキストカラーを変更
                    tv.setTextColor(Color.GRAY);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setSpinnerStockroom(ArrayAdapter<String> adapter) {
        // 初期選択を設定
        if (selectedStockroomName != null) {
            int position = adapter.getPosition(selectedStockroomName);
            if (position != -1) {
                spinnerStockroom.setSelection(position);
                selectedStockroom = findStockroomByName(selectedStockroomName);
            } else {
                Log.e(TAG, "選択された倉庫名がリストに存在しません: " + selectedStockroomName);
            }
        }
    }

    private void setDisplayName(TextView textDisplayName) {
        textDisplayName.setText(displayName);
    }

    // 名前からStockroomオブジェクトを検索するメソッド
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