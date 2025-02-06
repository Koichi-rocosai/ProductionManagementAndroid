package com.example.productionmanagementandroid.auth;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Workplace implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("shortName")
    private String shortName;
    @SerializedName("outsourcingId")
    private String outsourcingId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getOutsourcingId() {
        return outsourcingId;
    }
}