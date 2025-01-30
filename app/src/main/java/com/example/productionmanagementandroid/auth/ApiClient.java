package com.example.productionmanagementandroid.auth;

// 必要なセキュリティライブラリのインポート
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// Android用の警告抑制アノテーション
import android.annotation.SuppressLint;

// ネットワーク通信ライブラリのインポート
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

public class ApiClient {
    // APIのベースURL（HTTPSを使用）
    private static final String BASE_URL = "https://10.0.2.2:7246/";
    private static final String TAG = "ApiClient"; // ログ出力用タグ

    // Retrofitのインスタンス（シングルトン）
    private static Retrofit retrofit = null;

    @SuppressLint("CustomX509TrustManager")
    public static Retrofit getClient() {
        // すでにRetrofitインスタンスが存在する場合はそのまま返す
        if (retrofit == null) {
            try {
                // すべての証明書を信頼するカスタム TrustManager を作成
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            @SuppressLint("TrustAllX509TrustManager")
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                            @Override
                            @SuppressLint("TrustAllX509TrustManager")
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                };

                // TLSプロトコルを使用するSSLContextを作成し、すべての証明書を許可
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new SecureRandom());

                // カスタムOkHttpClientを作成（証明書の検証を無効化）
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier((hostname, session) -> true)
                        // HTTPリダイレクトを許可
                        .followRedirects(true)
                        // HTTPSリダイレクトを許可
                        .followSslRedirects(true)
                        .build();

                // Retrofitのインスタンスを作成し、カスタム OkHttpClient を適用
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)  // APIのベースURLを設定
                        .client(client)  // カスタムOkHttpClientを適用
                        .addConverterFactory(GsonConverterFactory.create())  // JSONデータの変換を設定
                        .build();

            } catch (Exception e) {
                // `printStackTrace()` の代わりに `Log.e()` を使用
                Log.e(TAG, "SSL設定エラー", e);
            }
        }
        // Retrofitのインスタンスを返す
        return retrofit;
    }
}
