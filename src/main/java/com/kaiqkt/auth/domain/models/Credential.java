package com.kaiqkt.auth.domain.models;

import io.azam.ulidj.ULID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "credential", schema = "PUBLIC")
public class Credential {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String hash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Credential() {
    }

    public Credential(User user, String hash) {
        this.id = ULID.random();
        this.user = user;
        this.hash = hash;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", hash='" + hash + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
