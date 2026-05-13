// src/main/java/tn/esprit/forum/entity/ReactionInfo.java
package tn.esprit.forum.entity;

import java.time.LocalDateTime;

public class ReactionInfo {
    private final int userId;
    private final String userName;
    private final ReactionType reaction;
    private final LocalDateTime createdAt;

    public ReactionInfo(int userId, String userName, ReactionType reaction, LocalDateTime createdAt) {
        this.userId = userId;
        this.userName = userName;
        this.reaction = reaction;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public ReactionType getReaction() { return reaction; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}