package com.example.productionmanagementandroid.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Stockroom implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Stockroom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Parcelable の実装
    protected Stockroom(Parcel in) {
        id = in.readInt();
        name = in.readString();
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
    }
}