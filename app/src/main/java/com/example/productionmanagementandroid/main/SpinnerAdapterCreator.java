package com.example.productionmanagementandroid.main;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.productionmanagementandroid.auth.Stockroom;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapterCreator {

    private static final String TAG = "SpinnerAdapterCreator";
    private static final String HINT_ITEM = "作業場所を選択"; // スピナーのヒント用アイテム
    private Context context;

    public SpinnerAdapterCreator(Context context) {
        this.context = context;
    }

    /**
     * 作業場所のリストからスピナー用のアダプターを作成する。
     *
     * @param stockrooms 作業場所のリスト。
     * @return スピナー用のアダプター。
     */
    public ArrayAdapter<String> createSpinnerAdapter(List<Stockroom> stockrooms) {
        Log.d(TAG, "createSpinnerAdapter: 開始");
        // スピナーに表示する作業場所の名前のリストを作成
        List<String> stockroomNames = new ArrayList<>();
        // ヒント用のアイテムを先頭に追加
        stockroomNames.add(HINT_ITEM);
        // 作業場所の名前をリストに追加
        for (Stockroom stockroom : stockrooms) {
            stockroomNames.add(stockroom.getName());
        }

        // スピナー用のアダプターを作成
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stockroomNames) {
            /**
             * スピナーのアイテムが選択可能かどうかを返す。
             *
             * @param position アイテムの位置。
             * @return アイテムが選択可能な場合は true、そうでない場合は false。
             */
            @Override
            public boolean isEnabled(int position) {
                Log.d(TAG, "createSpinnerAdapter: isEnabled: 開始");
                // ヒント用のアイテムを選択不可にする
                Log.d(TAG, "createSpinnerAdapter: isEnabled: 終了");
                return position != 0;
            }

            /**
             * スピナーのドロップダウンビューを取得する。
             *
             * @param position    アイテムの位置。
             * @param convertView 再利用可能な View オブジェクト。
             * @param parent      親 ViewGroup オブジェクト。
             * @return ドロップダウンビュー。
             */
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                Log.d(TAG, "createSpinnerAdapter: getDropDownView: 開始");
                // ドロップダウンビューを取得
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                // ヒント用のアイテムの場合
                if (position == 0) {
                    // ヒント用のアイテムのテキストカラーをグレーに変更
                    tv.setTextColor(Color.GRAY);
                } else {
                    // ヒント以外のアイテムのテキストカラーを黒に変更
                    tv.setTextColor(Color.BLACK);
                }
                Log.d(TAG, "createSpinnerAdapter: getDropDownView: 終了");
                return view;
            }
        };
        // ドロップダウンビューのリソースを設定
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d(TAG, "createSpinnerAdapter: 終了");
        return adapter;
    }
}