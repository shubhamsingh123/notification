package com.telus.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.model.BaseEvent;
import com.telus.notification.model.UserRegistrationEmailModel;
import com.telus.notification.entity.Notification;
import com.telus.notification.entity.UserNotificationDetails;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.repository.UserNotificationDetailsRepository;
import com.telus.notification.repository.NotificationTemplateRepository;
import com.telus.notification.exception.NotificationException;
import com.telus.notification.service.NotificationTemplateService;
import com.telus.notification.entity.NotificationTemplate;
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
import java.util.HashMap;

@Service
public class NotificationEventProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationEventProcessor.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationDetailsRepository userNotificationDetailsRepository;

@Autowired
private NotificationTemplateService templateService;

@Autowired
private ObjectMapper objectMapper;

@Autowired
private NotificationTemplateRepository notificationTemplateRepository;

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void handleMessage(Message<?> message) {
        logger.info("Received message from PubSub");
        String payload;
        Object rawPayload = message.getPayload();
        
        if (rawPayload instanceof byte[]) {
            payload = new String((byte[]) rawPayload);
            logger.info("Received byte[] payload, converted to String");
        } else if (rawPayload instanceof String) {
            payload = (String) rawPayload;
            logger.info("Received String payload");
        } else {
            logger.error("Unexpected payload type: {}", rawPayload.getClass());
            return;
        }
        
        logger.info("Message payload: {}", payload);
        
        try {
            BaseEvent event = objectMapper.readValue(payload, BaseEvent.class);
            logger.info("Successfully parsed payload into BaseEvent");
            logger.info("Event type: {}", event.getEventType());
            logger.info("Event data: {}", event.getData());
            processEvent(event);
        } catch (Exception e) {
            logger.error("Error processing PubSub message", e);
        }
    }

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

        // Create email model
        UserRegistrationEmailModel emailModel = new UserRegistrationEmailModel(
            username,
            email,
            managerEmail,
            LocalDateTime.now(),
            loginUrl
        );
        
        // Save or update the notification template
        String templateBody = String.format(
        	    "Welcome %s! You have been registered with role %s. Your email is %s and your status is %s.",
        	    username, role, email, status
        	);
        saveOrUpdateNotificationTemplate(event.getEventType(), "User Registration", templateBody);
        
        // Save user notification details
        UserNotificationDetails userDetails = new UserNotificationDetails();
        userDetails.setUserId(userId);
        userDetails.setUsername(username);
        userDetails.setEmail(email);
        userDetails.setRole(role);
        userDetails.setStatus(status);
        userDetails.setCreatedAt(LocalDateTime.now());
        
        userNotificationDetailsRepository.save(userDetails);
        logger.info("Saved user notification details for user: {}", username);

        logger.info("Sending registration email notification for user: {}", username);
        emailService.sendUserRegistrationEmail(emailModel, rmgEmail);

        // Process the template
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("role", role);
        variables.put("email", email);
        variables.put("status", status);
        
        NotificationTemplate template = templateService.getTemplateByEventType(event.getEventType());
        String message = templateService.processTemplate(template.getBodyTemplate(), variables);
        logger.info("Processed template message: '{}'", message);

        saveNotification(userId, "UserRegistered", message);
    }

    private void saveOrUpdateNotificationTemplate(String eventType, String name, String bodyTemplate) {
        NotificationTemplate template = templateService.getTemplateByEventType(eventType);
        if (template == null) {
            template = new NotificationTemplate();
            template.setEventType(eventType);
            template.setTemplateId(UUID.randomUUID().toString());
        }
        template.setName(name);
        template.setBodyTemplate(bodyTemplate);
        template.setUpdatedAt(LocalDateTime.now());
        notificationTemplateRepository.save(template);
        logger.info("Saved/Updated notification template for event type: {}", eventType);
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
