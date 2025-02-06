package com.example.productionmanagementandroid.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Stockroom implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("outsourcingId")
    private Integer outsourcingId; // Integer型に変更
    @SerializedName("workPlaceId")
    private int workPlaceId;

    public Stockroom(int id, String name, Integer outsourcingId, int workPlaceId) {
        this.id = id;
        this.name = name;
        this.outsourcingId = outsourcingId;
        this.workPlaceId = workPlaceId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getOutsourcingId() {
        return outsourcingId;
    }

    public int getWorkPlaceId() {
        return workPlaceId;
    }

    // Parcelable の実装
    protected Stockroom(Parcel in) {
        id = in.readInt();
        name = in.readString();
        // Integer型はreadInt()ではなく、readInt()で読み込み、0の場合はnullにする
        int tmpOutsourcingId = in.readInt();
        outsourcingId = tmpOutsourcingId == 0 ? null : tmpOutsourcingId;
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
        // Integer型はwriteInt()で書き込み、nullの場合は0を書き込む
        dest.writeInt(outsourcingId == null ? 0 : outsourcingId);
        dest.writeInt(workPlaceId);
    }
}