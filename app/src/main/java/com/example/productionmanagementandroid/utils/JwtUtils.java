package com.example.productionmanagementandroid.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JwtUtils {
    private static final String TAG = "JwtUtils"; // ログ出力用タグ

    /**
     * JWTトークンのペイロード部分をデコードする
     * @param token JWTトークン
     * @return デコードされたペイロード（JSONオブジェクト）
     */
    public static JSONObject decodeJwtPayload(String token) {
        try {
            // JWTは「ヘッダー.ペイロード.署名」の3部分に分かれている
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                Log.e(TAG, "無効なJWTトークン");
                return null;
            }

            // Base64URLエンコードされたペイロードをデコード
            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            // JSONオブジェクトとして返す
            return new JSONObject(decodedPayload);

        } catch (JSONException e) {
            Log.e(TAG, "JWTのデコードエラー", e);
        } catch (Exception e) {
            Log.e(TAG, "予期しないエラー", e);
        }
        return null;
    }
}
