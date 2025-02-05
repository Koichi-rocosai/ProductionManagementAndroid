package com.example.productionmanagementandroid.auth;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String displayName;
    private String outsourcingId;
    private String accessToken;

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
}