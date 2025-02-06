package com.example.productionmanagementandroid.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.productionmanagementandroid.R;
import com.example.productionmanagementandroid.auth.AuthManager;
import com.example.productionmanagementandroid.auth.User;

public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialog";
    private Spinner spinnerLoginType;
    private AuthManager authManager;
    private LoginSuccessListener loginSuccessListener;
    private Button buttonLogin;
    private Button buttonSelectArea;

    public interface LoginSuccessListener {
        void onLoginSuccess(User user, String selectedItem);
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
            // 作業場所が選択されている場合
            if (loginSuccessListener != null) {
                loginSuccessListener.onLoginSuccess(null, null); // ユーザー情報はまだないのでnull
            }
            dismiss(); // ダイアログを閉じる
        });

        Log.d(TAG, "LoginDialogFragment が表示された");

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }
}