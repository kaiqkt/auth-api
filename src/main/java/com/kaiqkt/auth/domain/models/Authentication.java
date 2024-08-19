package com.kaiqkt.auth.domain.models;

import java.time.LocalDateTime;

public class Authentication {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private LocalDateTime accessTokenExpiration;

    public Authentication() {
    }

    public Authentication(String accessToken, String refreshToken, String userId, LocalDateTime accessTokenExpiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(LocalDateTime accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId='" + userId + '\'' +
                ", accessTokenExpiration='" + accessTokenExpiration + '\'' +
                '}';
    }
}
