package com.kaiqkt.auth.domain.models;

import io.azam.ulidj.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "session", schema = "PUBLIC")
public class Session {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expireAt;
    private String userAgent;
    private String refreshToken;
    private String createdByIp;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "replaced_by")
    private Session replacedBy;
    private String revokedByIp;
    private LocalDateTime revokedAt;

    public Session() {
    }

    public Session(User user, LocalDateTime expireAt, String userAgent, String createdByIp) {
        this.id = ULID.random();
        this.user = user;
        this.expireAt = expireAt;
        this.userAgent = userAgent;
        this.createdByIp = createdByIp;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Session getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Session replacedBy) {
        this.replacedBy = replacedBy;
    }

    public String getRevokedByIp() {
        return revokedByIp;
    }

    public void setRevokedByIp(String revokedByIp) {
        this.revokedByIp = revokedByIp;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", expireAt=" + expireAt +
                ", userAgent='" + userAgent + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", createdByIp='" + createdByIp + '\'' +
                ", createdAt=" + createdAt +
                ", replacedBy='" + replacedBy + '\'' +
                ", revokedByIp='" + revokedByIp + '\'' +
                ", revokedAt=" + revokedAt +
                '}';
    }
}
