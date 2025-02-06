package com.example.productionmanagementandroid.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.productionmanagementandroid.R;
import com.example.productionmanagementandroid.auth.ApiClient;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.User;
import com.example.productionmanagementandroid.auth.Workplace;
import com.example.productionmanagementandroid.auth.WorkplaceApi;
import com.example.productionmanagementandroid.ui.adapters.WorkplaceAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialog";
    private Spinner spinnerLoginType;
    private AuthManager authManager;
    private LoginSuccessListener loginSuccessListener;
    private Button buttonLogin;
    private Button buttonSelectArea;
    private Workplace selectedWorkplace;

    public interface LoginSuccessListener {
        void onLoginSuccess(User user, int selectedWorkplaceId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginSuccessListener) {
            loginSuccessListener = (LoginSuccessListener) context;
        } else {
            throw new RuntimeException(context + " must implement LoginSuccessListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);

        EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        spinnerLoginType = view.findViewById(R.id.spinnerLoginType);
        buttonSelectArea = view.findViewById(R.id.buttonSelectArea);

        authManager = new AuthManager(requireContext());

        // 初期状態で spinnerLoginType と buttonSelectArea を非表示にする
        spinnerLoginType.setVisibility(View.GONE);
        buttonSelectArea.setVisibility(View.GONE);
        // 初期状態で buttonLogin のテキストを「ログインチェック」にする
        buttonLogin.setText(R.string.login_check_button);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            Log.d(TAG, "ログインボタンが押された - username: " + username);

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "ユーザー名とパスワードを入力してください", Toast.LENGTH_SHORT).show();
            } else {
                // ログイン処理を実行
                authManager.login(username, password, new AuthManager.LoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        // ログイン成功時の処理
                        Log.d(TAG, "ログイン成功");
                        Toast.makeText(getActivity(), "ログイン成功", Toast.LENGTH_SHORT).show();
                        // ログイン成功時に spinnerLoginType と buttonSelectArea を表示する
                        spinnerLoginType.setVisibility(View.VISIBLE);
                        buttonSelectArea.setVisibility(View.VISIBLE);
                        // buttonLogin のテキストを「ログイン」に変更する
                        buttonLogin.setText(R.string.login_button);
                        //作業場所のリストを取得
                        fetchWorkplaces(user);
                    }

                    @Override
                    public void onFailure(String message) {
                        // ログイン失敗時の処理
                        Log.e(TAG, "ログイン失敗: " + message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonSelectArea.setOnClickListener(v -> {
            if (selectedWorkplace != null) {
                // 作業場所が選択されている場合
                if (loginSuccessListener != null) {
                    loginSuccessListener.onLoginSuccess(null, selectedWorkplace.getId()); // ユーザー情報はまだないのでnull
                }
                dismiss(); // ダイアログを閉じる
            } else {
                // 作業場所が選択されていない場合
                Toast.makeText(getActivity(), "作業場所を選択してください", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "LoginDialogFragment が表示された");

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }

    private void fetchWorkplaces(User user) {
        Log.d(TAG, "fetchWorkplaces: 開始");
        // WorkplaceApi のインスタンスを作成
        WorkplaceApi workplaceApi = ApiClient.getClient().create(WorkplaceApi.class);
        // 作業場所のリストを取得する API リクエストを作成
        Call<List<Workplace>> call = workplaceApi.getWorkplaces("Bearer " + user.getAccessToken());

        // API リクエストを非同期で実行
        call.enqueue(new Callback<>() {
            /**
             * API レスポンスが成功した場合に呼び出される。
             *
             * @param call     API リクエストの Call オブジェクト。
             * @param response API レスポンスの Response オブジェクト。
             */
            @Override
            public void onResponse(@NonNull Call<List<Workplace>> call, @NonNull Response<List<Workplace>> response) {
                Log.d(TAG, "fetchWorkplaces: onResponse: 開始");
                // レスポンスが成功し、かつボディが null でない場合
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "fetchWorkplaces: onResponse: APIからのデータ取得に成功");
                    // レスポンスボディから作業場所のリストを取得
                    List<Workplace> workplaces = response.body();
                    // スピナー用のアダプターを作成
                    Log.d(TAG, "fetchWorkplaces: onResponse: スピナー用アダプター作成を開始");
                    WorkplaceAdapter adapter = new WorkplaceAdapter(requireContext(), workplaces);
                    Log.d(TAG, "fetchWorkplaces: onResponse: スピナー用アダプター作成に成功");
                    // ログインダイアログのスピナーにアダプターを設定
                    Log.d(TAG, "fetchWorkplaces: onResponse: ログインダイアログのスピナーにアダプターを設定");
                    spinnerLoginType.setAdapter(adapter);
                    spinnerLoginType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedWorkplace = (Workplace) parent.getItemAtPosition(position);
                            Log.d(TAG, "作業場所が選択されました: " + selectedWorkplace.getName());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedWorkplace = null;
                        }
                    });
                    Log.d(TAG, "fetchWorkplaces: onResponse: ログインダイアログのスピナーにアダプターを設定に成功");
                } else {
                    // API からのデータ取得に失敗した場合
                    Log.e(TAG, "fetchWorkplaces: onResponse: APIからのデータ取得に失敗: " + response.message());
                    Toast.makeText(getActivity(), "APIからのデータ取得に失敗", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "fetchWorkplaces: onResponse: 終了");
            }

            /**
             * API リクエストが失敗した場合に呼び出される。
             *
             * @param call API リクエストの Call オブジェクト。
             * @param t    API リクエストが失敗した理由を表す Throwable オブジェクト。
             */
            @Override
            public void onFailure(@NonNull Call<List<Workplace>> call, @NonNull Throwable t) {
                Log.e(TAG, "fetchWorkplaces: onFailure: 通信エラー: " + t.getMessage());
                Toast.makeText(getActivity(), "通信エラー",Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "fetchWorkplaces: 終了");
    }
}