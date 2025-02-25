package com.example.productionmanagementandroid.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.productionmanagementandroid.utils.JwtUtils;

public class AuthManager {
    private final Context context;
    private final SharedPreferences encryptedSharedPreferences; // EncryptedSharedPreferences を使用
    private static final String TAG = "AuthManager";
    private static final String PREF_NAME = "AuthPrefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String DISPLAY_NAME_KEY = "display_name";

    // コールバックインターフェース
    public interface LoginCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public AuthManager(Context context) {
        this.context = context;
        try {
            // MasterKey の作成
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // EncryptedSharedPreferences の初期化
            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "EncryptedSharedPreferences の初期化に失敗", e);
            throw new RuntimeException("EncryptedSharedPreferences の初期化に失敗", e);
        }
    }

    public void login(String username, String password, LoginCallback callback) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        LoginRequest request = new LoginRequest(username, password);

        Log.d(TAG, "ログインリクエスト送信: username=" + username + ", password=****");

        authApi.login(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.d(TAG, "HTTP Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    // EncryptedSharedPreferences を使用してトークンを保存
                    encryptedSharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply();

                    Log.d(TAG, "ログイン成功 - Token: " + token);
                    //Toast.makeText(context, "ログイン成功", Toast.LENGTH_SHORT).show(); // MainActivity に移動

                    decodeAndLogToken(token);
                    callback.onSuccess(); // ログイン成功を通知
                } else {
                    Log.e(TAG, "ログイン失敗 - HTTP Code: " + response.code() + ", Message: " + response.message());
                    //Toast.makeText(context, "ログイン失敗: " + response.message(), Toast.LENGTH_SHORT).show(); // MainActivity に移動
                    callback.onFailure("ログイン失敗: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "通信エラー: " + t.getMessage());
                //Toast.makeText(context, "通信エラー: " + t.getMessage(), Toast.LENGTH_LONG).show(); // MainActivity に移動
                callback.onFailure("通信エラー: " + t.getMessage());
            }
        });
    }

    private void decodeAndLogToken(String token) {
        JSONObject payload = JwtUtils.decodeJwtPayload(token);
        if (payload != null) {
            Log.d(TAG, "JWTペイロード: " + payload);

            String username = payload.optString("sub", "unknown");
            String displayName = payload.optString("DisplayName", "不明");
            String outsourcingId = payload.optString("OutsourcingId", "なし");

            Log.d(TAG, "ユーザー名(sub): " + username);
            Log.d(TAG, "表示名: " + displayName);
            Log.d(TAG, "アウトソーシングID: " + outsourcingId);

            saveDisplayName(displayName);
        } else {
            Log.e(TAG, "JWTの解析に失敗");
        }
    }

    private void saveDisplayName(String displayName) {
        encryptedSharedPreferences.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }

    public String getDisplayName() {
        return encryptedSharedPreferences.getString(DISPLAY_NAME_KEY, null);
    }

    public void logout() {
        encryptedSharedPreferences.edit().remove(ACCESS_TOKEN_KEY).remove(DISPLAY_NAME_KEY).apply();
    }

    public String getAccessToken() {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }
}