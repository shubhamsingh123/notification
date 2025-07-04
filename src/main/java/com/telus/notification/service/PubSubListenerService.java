package com.telus.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.model.UserRegisterEvent;
import com.telus.notification.model.AccountApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PubSubListenerService {
    
    private static final Logger logger = LoggerFactory.getLogger(PubSubListenerService.class);
    private final ObjectMapper objectMapper;
    private final NotificationEventProcessor notificationEventProcessor;

    @Autowired
    public PubSubListenerService(ObjectMapper objectMapper, NotificationEventProcessor notificationEventProcessor) {
        this.objectMapper = objectMapper;
        this.notificationEventProcessor = notificationEventProcessor;
    }

    public void receiveUserRegistrationMessage(String payload) {
        try {
            logger.info("Received user registration message: {}", payload);
            
            UserRegisterEvent event = objectMapper.readValue(payload, UserRegisterEvent.class);
            logger.info("Processed UserRegisterEvent: {}", event);
            
            // Process the event using NotificationEventProcessor
            notificationEventProcessor.processEvent(event.toBaseEvent());
            
        } catch (Exception e) {
            logger.error("Error processing user registration message", e);
            throw new RuntimeException("Failed to process user registration message", e);
        }
    }

    public void receiveAccountApprovedMessage(String payload) {
        try {
            logger.info("Received account approval message: {}", payload);
            
            AccountApprovedEvent event = objectMapper.readValue(payload, AccountApprovedEvent.class);
            logger.info("Processed AccountApprovedEvent: {}", event);
            
            // Process the event using NotificationEventProcessor
            notificationEventProcessor.processEvent(event.toBaseEvent());
            
        } catch (Exception e) {
            logger.error("Error processing account approval message", e);
            throw new RuntimeException("Failed to process account approval message", e);
        }
    }

    public void receiveAccountRejectMessage(String payload) {
        try {
            logger.info("Received account reject message: {}", payload);
            
            AccountApprovedEvent event = objectMapper.readValue(payload, AccountApprovedEvent.class);
            logger.info("Processed AccountRejectEvent: {}", event);
            
            // Process the event using NotificationEventProcessor
            notificationEventProcessor.processEvent(event.toBaseEvent());
            
        } catch (Exception e) {
            logger.error("Error processing account reject message", e);
            throw new RuntimeException("Failed to process account reject message", e);
        }
    }
}
