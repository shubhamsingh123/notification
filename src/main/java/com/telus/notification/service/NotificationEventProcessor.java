package com.telus.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.model.BaseEvent;
import com.telus.notification.entity.Notification;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.exception.NotificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

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

    @Autowired
    private ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void handleMessage(Message<?> message) {
        String payload;
        Object rawPayload = message.getPayload();
        
        if (rawPayload instanceof byte[]) {
            payload = new String((byte[]) rawPayload);
        } else if (rawPayload instanceof String) {
            payload = (String) rawPayload;
        } else {
            logger.error("Unexpected payload type: {}", rawPayload.getClass());
            return;
        }
        
        logger.info("Message received from PubSub: {}", payload);
        
        try {
            BaseEvent event = objectMapper.readValue(payload, BaseEvent.class);
            processEvent(event);
        } catch (Exception e) {
            logger.error("Error processing PubSub message", e);
        }
    }

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
            default:
                logger.warn("Unhandled event type: {}", event.getEventType());
        }
    }

    private void handleUserRegistered(BaseEvent event) {
        var data = event.getData();
        String userId = getRequiredField(data, "userId");
        String username = getRequiredField(data, "username");
        String email = getRequiredField(data, "email");
        String role = getRequiredField(data, "role");
        String status = getRequiredField(data, "status");
        String rmgEmail = getOptionalField(data, "rmgEmail", "shubham16cse06@gmail.com");

        String message = String.format(
            "New user %s has registered with role %s. Email: %s, Status: %s",
            username, role, email, status
        );
        
        logger.info("Generated notification for user registration: {}", message);
        emailService.sendSimpleMessage(
            rmgEmail,
            "New User Registration",
            message
        );

        saveNotification(userId, "UserRegistered", message);
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
