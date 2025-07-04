package com.telus.notification.model;

import java.time.LocalDateTime;

public class UserAccountApprovalEmailModel {

    private String userName;
    private String userEmail;
    private String managerEmail;
    private LocalDateTime registrationDate;
    private String loginUrl;

    public UserAccountApprovalEmailModel() {
    }

    public UserAccountApprovalEmailModel(String userName, String userEmail, String managerEmail, LocalDateTime registrationDate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.managerEmail = managerEmail;
        this.registrationDate = registrationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }


    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }
}
