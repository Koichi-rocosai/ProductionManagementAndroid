package com.example.productionmanagementandroid.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.utils.JwtUtils; // ✅ 修正: JwtUtils をインポート

public class AuthManager {
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private static final String TAG = "AuthManager"; // ログ出力用タグ

    public AuthManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
    }

    public void login(String username, String password) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        LoginRequest request = new LoginRequest(username, password);

        // APIリクエストをログ出力
        Log.d(TAG, "ログインリクエスト送信: username=" + username);

        // 🔹 ログ出力の改善 (デバッグ用にパスワードを "****" にマスク)
        Log.d(TAG, "ログインリクエスト送信: username=" + username + ", password=****");

        authApi.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                // HTTPレスポンスコードをログ出力
                Log.d(TAG, "HTTP Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    sharedPreferences.edit().putString("access_token", token).apply();

                    Log.d(TAG, "ログイン成功 - Token: " + token);
                    Toast.makeText(context, "ログイン成功", Toast.LENGTH_SHORT).show();

                    // 🔹 トークンをデコードして情報を取得
                    decodeAndLogToken(token);
                } else {
                    Log.e(TAG, "ログイン失敗 - HTTP Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(context, "ログイン失敗: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "通信エラー: " + t.getMessage());
                Toast.makeText(context, "通信エラー: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * JWTトークンをデコードしてユーザー情報をログ出力
     * @param token JWTトークン
     */
    private void decodeAndLogToken(String token) {
        JSONObject payload = JwtUtils.decodeJwtPayload(token);
        if (payload != null) {
            Log.d(TAG, "JWTペイロード: " + payload.toString());

            // ユーザー情報を取得
            String username = payload.optString("sub", "unknown");
            String displayName = payload.optString("DisplayName", "不明");
            String outsourcingId = payload.optString("OutsourcingId", "なし");

            Log.d(TAG, "ユーザー名(sub): " + username);
            Log.d(TAG, "表示名: " + displayName);
            Log.d(TAG, "アウトソーシングID: " + outsourcingId);
        } else {
            Log.e(TAG, "JWTの解析に失敗");
        }
    }

    public String getToken() {
        return sharedPreferences.getString("access_token", null);
    }

    public void logout() {
        sharedPreferences.edit().remove("access_token").apply();
        Log.d(TAG, "ログアウト成功");
    }
}