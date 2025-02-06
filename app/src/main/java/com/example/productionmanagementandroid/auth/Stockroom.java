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
    private int outsourcingId; // 追加
    @SerializedName("workPlaceId")
    private int workPlaceId; // 追加

    public int getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public int getOutsourcingId() {
        return outsourcingId;
    }

    public int getWorkPlaceId() {
        return workPlaceId;
    }

    // Parcelable の実装
    protected Stockroom(Parcel in) {
        id = in.readInt();
        name = in.readString();
        outsourcingId = in.readInt(); // 追加
        workPlaceId = in.readInt(); // 追加
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
        dest.writeInt(outsourcingId); // 追加
        dest.writeInt(workPlaceId); // 追加
    }
}