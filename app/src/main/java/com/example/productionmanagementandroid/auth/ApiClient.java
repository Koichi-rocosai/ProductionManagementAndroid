package com.example.productionmanagementandroid.auth;

import android.annotation.SuppressLint;
import android.util.Log;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class ApiClient {
    private static final String BASE_URL = "https://10.0.2.2:7246/";
//    private static final String BASE_URL = "http://192.168.31.171/";
    private static final String TAG = "ApiClient";
    private static volatile Retrofit retrofit = null; // volatile を追加

    @SuppressLint({"CustomX509TrustManager", "TrustAllX509TrustManager"})
    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) { // synchronized ブロックを追加
                if (retrofit == null) {
                    try {
                        // 開発環境用: すべての証明書を信頼する TrustManager (本番環境では使用しないでください)
                        TrustManager[] trustAllCerts = new TrustManager[]{
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                                    }

                                    @Override
                                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                                    }

                                    @Override
                                    public X509Certificate[] getAcceptedIssuers() {
                                        return new X509Certificate[]{};
                                    }
                                }
                        };

                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, new SecureRandom());

                        OkHttpClient client = new OkHttpClient.Builder()
                                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                                .hostnameVerifier((hostname, session) -> true)
                                .followRedirects(true)
                                .followSslRedirects(true)
                                .build();

                        retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                    } catch (Exception e) {
                        Log.e(TAG, "SSL設定エラー: " + e.getMessage(), e);
                    }
                }
            }
        }
        return retrofit;
    }
}