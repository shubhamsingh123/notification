package com.telus.notification.dto;

public class NotificationTemplate {
    private String id;
    private String name;
    private String eventType;
    private String subject;

    // Constructor
    public NotificationTemplate(String id, String name, String eventType, String subject) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
        this.subject = subject;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
