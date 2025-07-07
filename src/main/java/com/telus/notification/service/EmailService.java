package com.telus.notification.service;

import com.telus.notification.model.UserAccountApprovalEmailModel;
import com.telus.notification.model.UserRegistrationEmailModel;

public interface EmailService {
    /**
     * Sends a user registration email using HTML template.
     * @param model The UserRegistrationEmailModel containing user and registration details
     * @param rmgEmail The email address of the Resource Management Group (primary recipient)
     */
    void sendUserRegistrationEmail(UserRegistrationEmailModel model, String rmgEmail);

    
    void sendAccountApproveEmail(UserAccountApprovalEmailModel model, String rmgEmail);

    /**
     * Sends an account rejection email using HTML template.
     * @param model The UserAccountApprovalEmailModel containing user details
     * @param rmgEmail The email address of the Resource Management Group (primary recipient)
     */
    void sendAccountRejectionEmail(UserAccountApprovalEmailModel model, String rmgEmail);

    /**
     * Sends a simple text email message.
     * @param to recipient email address
     * @param subject email subject
     * @param text email content
     */
    void sendSimpleMessage(String to, String subject, String text);

    /**
     * Sends an HTML email message.
     * @param to recipient email address
     * @param subject email subject
     * @param htmlContent email content in HTML format
     */
    void sendHtmlEmail(String to, String subject, String htmlContent);
}
