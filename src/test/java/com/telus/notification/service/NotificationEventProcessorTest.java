package com.telus.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.model.BaseEvent;
import com.telus.notification.entity.NotificationTemplate;
import com.telus.notification.repository.NotificationTemplateRepository;
import java.time.LocalDateTime;

@SpringBootTest
public class NotificationEventProcessorTest {

    @Autowired
    private NotificationEventProcessor notificationEventProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @BeforeEach
    public void setup() {
        // Create and save a test template
        NotificationTemplate template = new NotificationTemplate();
        template.setEventType("UserRegistered");
        template.setName("User Registration Template");
        template.setBodyTemplate("Welcome ${userName}! Your account has been created.");
        template.setSubjectTmp("Welcome to TELUS");
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        notificationTemplateRepository.save(template);
    }

    @Test
    public void testProcessEvent() throws Exception {
        // Create dummy JSON payload for UserRegisterEvent event
        String jsonPayload = """
            {
                "eventType": "UserRegistered",
                "timestamp": "2025-06-26T12:30:45.123Z",
                "data": {
                    "userId": "12345",
                    "username": "john.doe",
                    "email": "john.doe@example.com",
                    "role": "Manager",
                    "status": "pending",
                    "rmgEmail": "shubham16cse06@gmail.com"
                }
            }
            """;

        // Convert JSON string to BaseEvent object
        BaseEvent event = objectMapper.readValue(jsonPayload, BaseEvent.class);

        // Call processEvent method
        notificationEventProcessor.processEvent(event);
    }
}
