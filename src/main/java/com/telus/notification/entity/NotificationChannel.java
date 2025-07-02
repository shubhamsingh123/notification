package com.telus.notification.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION_CHANNELS")
public class NotificationChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "configuration", nullable = false, columnDefinition = "VARCHAR(4000)")
    private String configuration;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum ChannelType {
        EMAIL, SMS, PUSH, IN_APP
    }

    // Getters
    public Integer getChannelId() {
        return channelId;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
