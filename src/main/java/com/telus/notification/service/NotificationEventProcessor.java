package com.telus.notification.service;

import com.telus.notification.model.BaseEvent;
import com.telus.notification.entity.Notification;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.exception.NotificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationEventProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationEventProcessor.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    public void processEvent(BaseEvent event) {
        if (event == null) {
            throw new NotificationException("Event cannot be null");
        }
        if (event.getEventType() == null || event.getEventType().trim().isEmpty()) {
            throw new NotificationException("Event type cannot be null or empty");
        }
        if (event.getData() == null || event.getData().isEmpty()) {
            throw new NotificationException("Event data cannot be null or empty");
        }

        logger.info("Processing event of type: {}", event.getEventType());
        
        switch(event.getEventType()) {
            case "UserRegistered":
                handleUserRegistered(event);
                break;
            case "AccountApproved":
                handleAccountApproved(event);
                break;
            case "NOTIFICATION_CREATED":
                handleNotificationCreated(event);
                break;
            default:
                logger.warn("Unhandled event type: {}", event.getEventType());
        }
    }

    private void handleUserRegistered(BaseEvent event) {
        var data = event.getData();
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String role = getRequiredField(data, "role");
        String status = getRequiredField(data, "status");

        // TODO: Generate notification using template
        String message = String.format(
            "Welcome %s! Your account has been created with role %s. Current status: %s",
            username, role, status
        );
        
        logger.info("Generated notification for user registration: {}", message);
        emailService.sendSimpleMessage(
            email,
            "Welcome to Our Service",
            message
        );

        saveNotification(username, "UserRegistered", message);
    }

    private void handleAccountApproved(BaseEvent event) {
        var data = event.getData();
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String approvedBy = getRequiredField(data, "approvedBy");

        // TODO: Generate notification using template
        String message = String.format(
            "Hello %s, Your account has been approved by %s. You can now access all features of the system.",
            username, approvedBy
        );
        
        logger.info("Generated notification for account approval: {}", message);
        emailService.sendSimpleMessage(
            email,
            "Account Approved",
            message
        );

        saveNotification(username, "AccountApproved", message);
    }

    private void saveNotification(String externalUserId, String type, String message) {
        Notification notification = new Notification();
        notification.setNotificationId(UUID.randomUUID().toString());
        notification.setExternalUserId(externalUserId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        notification.setIsRead(false);

        notificationRepository.save(notification);
        logger.info("Notification saved to database for user: {}", externalUserId);
    }

    private void handleNotificationCreated(BaseEvent event) {
        var data = event.getData();
        String externalUserId = getRequiredField(data, "externalUserId");
        String message = getRequiredField(data, "message");
        String type = getRequiredField(data, "type");

        saveNotification(externalUserId, type, message);
        logger.info("Created notification for user: {}", externalUserId);
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
