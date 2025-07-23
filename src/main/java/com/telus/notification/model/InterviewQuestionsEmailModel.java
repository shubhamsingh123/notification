package com.telus.notification.model;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewQuestionsEmailModel {
    private String candidateName;
    private String managerEmail;
    private String position;
    private LocalDateTime interviewDate;
    private List<String> questions;
    private String interviewMode;

    public InterviewQuestionsEmailModel(String candidateName, String managerEmail, String position, 
                                      LocalDateTime interviewDate, List<String> questions) {
        this.candidateName = candidateName;
        this.managerEmail = managerEmail;
        this.position = position;
        this.interviewDate = interviewDate;
        this.questions = questions;
        this.interviewMode = "Online (MS Teams)"; // Default value
    }

    public InterviewQuestionsEmailModel(String candidateName, String managerEmail, String position, 
                                      LocalDateTime interviewDate, List<String> questions, String interviewMode) {
        this.candidateName = candidateName;
        this.managerEmail = managerEmail;
        this.position = position;
        this.interviewDate = interviewDate;
        this.questions = questions;
        this.interviewMode = interviewMode;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public String getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(String interviewMode) {
        this.interviewMode = interviewMode;
    }
}
