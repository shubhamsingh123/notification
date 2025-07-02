package com.telus.notification.model;

import java.time.Instant;
import java.util.Map;

public class BaseEvent {
    private String eventType;
    private Instant timestamp;
    private Map<String, Object> data;

    /**
     * For UserRegisterEventEvent, the data map should contain:
     * - userId: String
     * - username: String
     * - email: String
     * - role: String
     * - status: String
     * - rmgEmail: String
     */

    // Getters and setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
