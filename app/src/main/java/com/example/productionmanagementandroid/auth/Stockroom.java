package com.example.productionmanagementandroid.auth;

import com.google.gson.annotations.SerializedName;

public class Stockroom {
    @SerializedName("id")
    private int id; // private フィールド 'id' は代入されません: id フィールドに値が代入されていないことを示しています。
    @SerializedName("name")
    private String name; // private フィールド 'name' は代入されません: name フィールドに値が代入されていないことを示しています。

    // メソッド 'getId()' は使用されません: getId() メソッドがどこからも呼び出されていないことを示しています。
    // 警告の原因: これらの警告は、Stockroom クラスのフィールド (id と name) が、コンストラクタや他のメソッドで初期化されていないことが原因です。
    // また、getId() メソッドがどこからも呼び出されていないため、未使用と判断されています。
    // 修正方針:
    // フィールドの初期化:
    // Stockroom クラスにコンストラクタを追加し、id と name フィールドを初期化するようにします。
    // ただし、今回はAPIから取得したJSONデータをGsonライブラリで変換して使用するため、コンストラクタは不要です。
    // Gsonライブラリは、@SerializedNameアノテーションに基づいて、JSONデータを自動的にフィールドに代入します。
    // getId() メソッドの使用:
    // getId() メソッドは、現状では使用されていませんが、将来的に使用される可能性があるので、削除せずに残します。
    // Stockroom クラスの id フィールドは、一意の識別子として使用されることが想定されるため、getId() メソッドを残すことは理にかなっています。
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}