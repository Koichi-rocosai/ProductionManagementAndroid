package com.example.productionmanagementandroid.auth;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String displayName;
    private String outsourcingId;
    private String accessToken;
    private int workplaceId; // 追加

    // コンストラクタ、ゲッター、セッターなど
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOutsourcingId() {
        return outsourcingId;
    }

    public void setOutsourcingId(String outsourcingId) {
        this.outsourcingId = outsourcingId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // 追加
    public int getWorkplaceId() {
        return workplaceId;
    }

    // 追加
    public void setWorkplaceId(int workplaceId) {
        this.workplaceId = workplaceId;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", outsourcingId='" + outsourcingId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", workplaceId=" + workplaceId +
                '}';
    }
}