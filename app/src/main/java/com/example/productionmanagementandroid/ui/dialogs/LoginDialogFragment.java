package com.example.productionmanagementandroid.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.productionmanagementandroid.R;

public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = "LoginDialog";
    private Spinner spinnerLoginType;
    private LoginDialogListener listener;
    private String selectedStockroomName = null; // 選択された作業場所の名前を保持

    // コールバックインターフェース
    public interface LoginDialogListener {
        void onLogin(String username, String password, String selectedStockroomName); // 作業場所の情報を追加
    }

    // コールバックリスナーを設定するメソッド
    public void setLoginDialogListener(LoginDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.activity_login, null); // レイアウトファイルを修正

        EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        spinnerLoginType = view.findViewById(R.id.spinnerLoginType);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            Log.d(TAG, "ログインボタンが押された - username: " + username);

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "ユーザー名とパスワードを入力してください", Toast.LENGTH_SHORT).show();
            } else {
                if (listener != null) {
                    listener.onLogin(username, password, selectedStockroomName); // コールバックを呼び出す
                }
                dismiss(); // ダイアログを閉じる
            }
        });

        Log.d(TAG, "LoginDialogFragment が表示された");

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }

    public void setSpinnerAdapter(ArrayAdapter<String> adapter) {
        if (spinnerLoginType != null) {
            spinnerLoginType.setAdapter(adapter);
            spinnerLoginType.setSelection(0); // 初期選択をヒントにする
            spinnerLoginType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        // ヒントが選択された場合は何もしない
                        selectedStockroomName = null; // ヒントが選択された場合は null を設定
                        Log.d(TAG, "ヒントが選択されました");
                    } else {
                        // ヒント以外のアイテムが選択された場合の処理
                        selectedStockroomName = (String) parent.getItemAtPosition(position); // 選択された作業場所の名前を保持
                        Log.d(TAG, "作業場所が選択されました: " + selectedStockroomName);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // 何も選択されなかった場合の処理
                    selectedStockroomName = null; // 何も選択されなかった場合は null を設定
                }
            });
        }
    }
}