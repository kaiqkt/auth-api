package com.kaiqkt.auth.domain.models;

import com.kaiqkt.auth.domain.models.enums.VerificationType;
import io.azam.ulidj.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification", schema = "PUBLIC")
public class Verification {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String token;
    private LocalDateTime expireAt;
    @Enumerated(EnumType.STRING)
    private VerificationType type;

    public Verification() {
    }

    public Verification(User user, String token, LocalDateTime expireAt, VerificationType type) {
        this.id = ULID.random();
        this.user = user;
        this.token = token;
        this.expireAt = expireAt;
        this.type = type;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public VerificationType getType() {
        return type;
    }

    public void setType(VerificationType purpose) {
        this.type = purpose;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", expireAt=" + expireAt +
                ", purpose='" + type + '\'' +
                '}';
    }
}
