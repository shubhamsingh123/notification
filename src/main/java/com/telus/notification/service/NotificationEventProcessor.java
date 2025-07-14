package com.telus.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.model.BaseEvent;
import com.telus.notification.model.UserAccountApprovalEmailModel;
import com.telus.notification.model.UserRegistrationEmailModel;
import com.telus.notification.entity.Notification;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.repository.NotificationTemplateRepository;
import com.telus.notification.exception.NotificationException;
import com.telus.notification.service.NotificationTemplateService;
import com.telus.notification.entity.NotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class NotificationEventProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationEventProcessor.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;


@Autowired
private NotificationTemplateService templateService;

@Autowired
private ObjectMapper objectMapper;

@Autowired
private NotificationTemplateRepository notificationTemplateRepository;

    public void processEvent(BaseEvent event) {
        logger.info("Entering processEvent method");
        if (event == null) {
            logger.error("Event is null");
            throw new NotificationException("Event cannot be null");
        }
        if (event.getEventType() == null || event.getEventType().trim().isEmpty()) {
            logger.error("Event type is null or empty");
            throw new NotificationException("Event type cannot be null or empty");
        }
        if (event.getData() == null || event.getData().isEmpty()) {
            logger.error("Event data is null or empty");
            throw new NotificationException("Event data cannot be null or empty");
        }

        logger.info("Processing event of type: {}", event.getEventType());
        logger.debug("Event data: {}", event.getData());
        
        switch (event.getEventType()) {
        case "UserRegistered":
            handleUserRegisterEvent(event);
            break;

        case "AccountApproved":
            handleAccountApproved(event);
            break;

        case "AccountRejected":
            handleAccountReject(event);
            break;

        default:
            logger.warn("Unhandled event type: {}", event.getEventType());
    }
    }

    private void handleUserRegisterEvent(BaseEvent event) {
        var data = event.getData();
        logger.info("Processing UserRegistered event with data: {}", data);
        
        String userId = getRequiredField(data, "userId");
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String role = getRequiredField(data, "role");
        String status = getRequiredField(data, "status");
        String rmgEmail = getOptionalField(data, "rmgEmail", "shubham16cse06@gmail.com");
        String managerEmail = getOptionalField(data, "managerEmail", email); // Default to user's email if not provided
        String loginUrl = getOptionalField(data, "loginUrl", "https://telus.com/login"); // Default login URL

        logger.info("Extracted fields from event - username: {}, role: {}, email: {}, status: {}", 
            username, role, email, status);

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", username);
        variables.put("userId", email);
        variables.put("loginUrl", loginUrl);
        variables.put("currentYear", Year.now().toString());
        
        NotificationTemplate template = templateService.getTemplateByEventType(event.getEventType());
        if (template == null) {
            logger.error("No template found for event type: {}", event.getEventType());
            throw new NotificationException("Template not found for event type: " + event.getEventType());
        }
        
        String message = templateService.processTemplate(template.getBodyTemplate(), variables);
        logger.info("Processed template message: '{}'", message);

        // Create email model
        UserRegistrationEmailModel emailModel = new UserRegistrationEmailModel(
            username,
            email,
            managerEmail,
            LocalDateTime.now(),
            loginUrl
        );
        
        logger.info("Sending registration email notification for user: {}", username);
        emailService.sendUserRegistrationEmail(emailModel, rmgEmail);

        saveNotification(userId, "UserRegistered", "New user " + username + "'s account is registered.", true);
    }
    
    private void handleAccountApproved(BaseEvent event) {
        var data = event.getData();
        logger.info("Processing AccountApproved event with data: {}", data);
        
        String userId = getRequiredField(data, "userId");
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String rmgEmail = getOptionalField(data, "rmgEmail", "shubham16cse06@gmail.com");
        String managerEmail = getOptionalField(data, "managerEmail", email); // Default to user's email if not provided

        logger.info("Extracted fields from event - username: {}, email: {}", username, email);

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", username);
        variables.put("userId", email);
        variables.put("currentYear", Year.now().toString());

        // Create email model
        UserAccountApprovalEmailModel approvedEmailModel = new UserAccountApprovalEmailModel(
            username,
            email,
            managerEmail,
            LocalDateTime.now(),
            null  // Passing null for rejectionReason as it's not applicable for approval
        );
        
        logger.info("Sending account approval email notification for user: {}", username);
        emailService.sendAccountApproveEmail(approvedEmailModel, rmgEmail);

        saveNotification(userId,"AccountApproved" , "Your manager account is approved by " + rmgEmail, false);
    }

    
    private void handleAccountReject(BaseEvent event) {
        var data = event.getData();
        logger.info("Processing AccountReject event with data: {}", data);
        
        String userId = getRequiredField(data, "userId");
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String rmgEmail = getOptionalField(data, "rmgEmail", "shubham16cse06@gmail.com");
        String managerEmail = getOptionalField(data, "managerEmail", email); // Default to user's email if not provided
        String rejectionReason = getOptionalField(data, "rejectionReason", "No reason provided");

        logger.info("Extracted fields from event - username: {}, email: {}, rejectionReason: {}", username, email, rejectionReason);

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", username);
        variables.put("userId", email);
        variables.put("currentYear", Year.now().toString());
        
        // Create email model
        UserAccountApprovalEmailModel rejectedEmailModel = new UserAccountApprovalEmailModel(
            username,
            email,
            managerEmail,
            LocalDateTime.now(),
            rejectionReason
        );
        
        logger.info("Sending account rejection email notification for user: {}", username);
        emailService.sendAccountRejectionEmail(rejectedEmailModel, rmgEmail);

        saveNotification(userId, "AccountReject", "Your account has been rejected by " + rmgEmail + ". Reason: " + rejectionReason, false);
    }

    
    private void updateNotificationTemplate(String eventType, Map<String, Object> data) {
        try {
            NotificationTemplate updatedTemplate = templateService.updateTemplateBody(eventType, data);
            logger.info("Updated notification template for event type: {}", eventType);
            logger.debug("Updated template: {}", updatedTemplate);
        } catch (RuntimeException e) {
            logger.error("Error updating template for event type: {}", eventType, e);
            throw new NotificationException("Error updating template: " + e.getMessage());
        }
    }

    private String getDefaultHTMLTemplate() {
        return "<!DOCTYPE html>\n<html xmlns:th=\"http://www.thymeleaf.org\">\n<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Welcome to TELUS</title>\n" +
               "    <style>\n" +
               "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333333; margin: 0; padding: 0; }\n" +
               "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
               "        .header { background-color: #4B286D; padding: 20px; text-align: center; }\n" +
               "        .header img { max-width: 150px; }\n" +
               "        .content { padding: 20px; background-color: #ffffff; }\n" +
               "        .footer { background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; }\n" +
               "        .button { display: inline-block; padding: 10px 20px; background-color: #66CC00; color: #ffffff; text-decoration: none; border-radius: 5px; margin: 20px 0; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <div class=\"header\">\n" +
               "            <img src=\"cid:telus-logo\" alt=\"TELUS Logo\">\n" +
               "        </div>\n" +
               "        <div class=\"content\">\n" +
               "            <h2>Welcome to TELUS!</h2>\n" +
               "            <p>Dear ${userName},</p>\n" +
               "            <p>Thank you for registering with TELUS. We're excited to have you on board!</p>\n" +
               "            <p>Your account has been successfully created with the following details:</p>\n" +
               "            <ul>\n" +
               "                <li>Username: ${userName}</li>\n" +
               "                <li>Email: ${userId}</li>\n" +
               "            </ul>\n" +
               "            <p>What's Next?</p>\n" +
               "            <ul>\n" +
               "                <li>Complete your profile</li>\n" +
               "                <li>Explore our services</li>\n" +
               "                <li>Contact support if you need assistance</li>\n" +
               "            </ul>\n" +
               "            <a href=\"${loginUrl}\" class=\"button\">Login to Your Account</a>\n" +
               "            <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>\n" +
               "            <p>Best regards,<br>The TELUS Team</p>\n" +
               "        </div>\n" +
               "        <div class=\"footer\">\n" +
               "            <p>This email was sent to ${userId}. Please do not reply to this email.</p>\n" +
               "            <p>&copy; ${currentYear} TELUS. All rights reserved.</p>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }

    private String getOptionalField(Map<String, Object> data, String fieldName, String defaultValue) {
        Object value = data.get(fieldName);
        if (value == null) {
            return defaultValue;
        }
        if (!(value instanceof String)) {
            return defaultValue;
        }
        String stringValue = (String) value;
        return stringValue.trim().isEmpty() ? defaultValue : stringValue;
    }

    private void saveNotification(String externalUserId, String type, String message, boolean isUserRegistered) {
        Notification notification = new Notification();
        notification.setExternalUserId(isUserRegistered ? "1" : externalUserId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedBy("SYSTEM");
        notification.setUpdatedBy("SYSTEM");
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification saved to database for user: {} with ID: {}", 
                    notification.getExternalUserId(), savedNotification.getNotificationId());
    }

    private String getRequiredField(Map<String, Object> data, String fieldName) {
        Object value = data.get(fieldName);
        if (value == null) {
            throw new NotificationException("Required field '" + fieldName + "' is missing from event data");
        }
        if (!(value instanceof String)) {
            throw new NotificationException("Field '" + fieldName + "' must be a string");
        }
        String stringValue = (String) value;
        if (stringValue.trim().isEmpty()) {
            throw new NotificationException("Field '" + fieldName + "' cannot be empty");
        }
        return stringValue;
    }
}
