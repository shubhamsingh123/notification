package com.telus.notification.model;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterEvent {
    private String userId;
    private String username;
    private String email;
    private String role;
    private String status;
    private String rmgEmail;
    private String managerEmail;
    private String loginUrl;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRmgEmail() {
        return rmgEmail;
    }

    public void setRmgEmail(String rmgEmail) {
        this.rmgEmail = rmgEmail;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public BaseEvent toBaseEvent() {
        BaseEvent baseEvent = new BaseEvent();
        baseEvent.setEventType("UserRegistered");
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", this.userId);
        data.put("username", this.username);
        data.put("email", this.email);
        data.put("role", this.role);
        data.put("status", this.status);
        data.put("rmgEmail", this.rmgEmail);
        data.put("managerEmail", this.managerEmail);
        data.put("loginUrl", this.loginUrl);
        
        baseEvent.setData(data);
        return baseEvent;
    }

    @Override
    public String toString() {
        return "UserRegisterEvent{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", rmgEmail='" + rmgEmail + '\'' +
                ", managerEmail='" + managerEmail + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                '}';
    }
}
