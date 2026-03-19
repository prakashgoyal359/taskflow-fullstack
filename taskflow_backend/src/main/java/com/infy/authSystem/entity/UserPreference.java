package com.infy.authSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    private Long userId;

    @Column(name = "avatar_colour")
    private String avatarColour;

    @Column(name = "bio")
    private String bio;

    // getters and setters

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAvatarColour() { return avatarColour; }
    public void setAvatarColour(String avatarColour) { this.avatarColour = avatarColour; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
