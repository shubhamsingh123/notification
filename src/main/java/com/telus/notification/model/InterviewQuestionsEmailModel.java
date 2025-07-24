package com.telus.notification.model;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewQuestionsEmailModel {
    private String candidateName;
private List<String> managerEmails;
    private String position;
    private LocalDateTime interviewDate;
    private String questions;
    private String interviewMode;

    public InterviewQuestionsEmailModel(String candidateName, List<String> managerEmails, String position,
                                    LocalDateTime interviewDate, String questions) {
    this.candidateName = candidateName;
    this.managerEmails = managerEmails;
    this.position = position;
    this.interviewDate = interviewDate;
    this.questions = questions;
    this.interviewMode = "Online (MS Teams)";
}

public InterviewQuestionsEmailModel(String candidateName, List<String> managerEmails, String position,
                                    LocalDateTime interviewDate, String questions, String interviewMode) {
    this.candidateName = candidateName;
    this.managerEmails = managerEmails;
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

    public List<String> getManagerEmails() {
        return managerEmails;
    }

    public void setManagerEmails(List<String> managerEmails) {
        this.managerEmails = managerEmails;
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

    

public String getQuestions() {
    return questions;
}

public void setQuestions(String questions) {
    this.questions = questions;
}

    public String getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(String interviewMode) {
        this.interviewMode = interviewMode;
    }
}
