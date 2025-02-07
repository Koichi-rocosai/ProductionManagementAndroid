package com.example.productionmanagementandroid.auth;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Stockroom implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    @Nullable
    private String name;
    @SerializedName("outsourcingId")
    @Nullable
    private String outsourcingId; // int から String に変更
    @SerializedName("workPlaceId")
    private int workPlaceId;

    public int getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getOutsourcingId() { // 戻り値を String に変更
        return outsourcingId;
    }

    public int getWorkPlaceId() {
        return workPlaceId;
    }

    // Parcelable の実装
    protected Stockroom(Parcel in) {
        id = in.readInt();
        name = in.readString();
        outsourcingId = in.readString(); // int から String に変更
        if (outsourcingId == null) {
            outsourcingId = ""; // nullの場合は空文字に設定
        }
        workPlaceId = in.readInt();
    }

    public static final Creator<Stockroom> CREATOR = new Creator<Stockroom>() {
        @Override
        public Stockroom createFromParcel(Parcel in) {
            return new Stockroom(in);
        }

        @Override
        public Stockroom[] newArray(int size) {
            return new Stockroom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(outsourcingId); // int から String に変更
        dest.writeInt(workPlaceId);
    }
}