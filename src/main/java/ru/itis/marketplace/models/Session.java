package ru.itis.marketplace.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session implements Entity<UUID>{
    private UUID sessionId;
    private final Long userId;
    private final LocalDateTime expireAt;

    public Session(UUID sessionId, Long userId, LocalDateTime expireAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expireAt = expireAt;
    }

    public Session(Long userId, LocalDateTime expireAt) {
        this.sessionId = UUID.randomUUID();
        this.userId = userId;
        this.expireAt = expireAt;
    }

    @Override
    public UUID getId() {
        return sessionId;
    }

    @Override
    public void setId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
}
