package com.example.productionmanagementandroid.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productionmanagementandroid.R;
import com.example.productionmanagementandroid.auth.AuthManager;

public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);

        EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            Log.d(TAG, "ログインボタンが押された - username: " + username);

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "ユーザー名とパスワードを入力してください", Toast.LENGTH_SHORT).show();
            } else {
                AuthManager authManager = new AuthManager(requireContext());
                authManager.login(username, password);
                dismiss(); // ダイアログを閉じる
            }
        });

        Log.d(TAG, "LoginDialogFragment が表示された");

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }
}
