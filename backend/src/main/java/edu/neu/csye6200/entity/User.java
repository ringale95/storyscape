package edu.neu.csye6200.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

import edu.neu.csye6200.dto.UserRegisterDTO;

@Entity
@ToString
@Table(name = "users", indexes = { @Index(name = "idx_user_username", columnList = "username") })
public class User {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // or UUID if you want to match your schema

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @ToString.Exclude
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String bio;

    public User(UserRegisterDTO dto, String hashedPassword) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.passwordHash = hashedPassword;
        this.bio = dto.getBio();
        this.tier = Tier.valueOf(dto.getTier().toUpperCase());
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    @ToString.Exclude
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier = Tier.NORMAL; // normal, other, core

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, SUSPENDED, DELETED

    @Column(name = "followers_count")
    private Integer followersCount = 0;

    @Column(name = "following_count")
    private Integer followingCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

    // Ensure default values and timestamps are set before persisting/updating
    @PrePersist
    protected void onCreate() {
        if (this.tier == null)
            this.tier = Tier.NORMAL;
        if (this.status == null)
            this.status = "ACTIVE";
        if (this.followersCount == null)
            this.followersCount = 0;
        if (this.followingCount == null)
            this.followingCount = 0;
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", tier='" + tier + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
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
}
