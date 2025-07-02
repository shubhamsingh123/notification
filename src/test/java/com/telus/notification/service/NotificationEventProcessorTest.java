package com.telus.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class NotificationEventProcessorTest {

    @Autowired
    private NotificationEventProcessor notificationEventProcessor;

    @Test
    public void testHandleMessage() throws Exception {
        // Create dummy JSON payload for UserRegisterEvent event
        String jsonPayload = """
            {
"eventType": "UserRegisterEvent",
"timestamp": "2025-06-26T12:30:45.123Z",
"data": {
"userId": "12345",
"username": "john.doe",
"email": "john.doe@example.com",
"role": "Manager",
"status": "pending",
"rmgEmail" : "shubham16cse06@gmail.com"
}
}
            """;

        // Convert JSON string to byte array (simulating PubSub message)
        byte[] payload = jsonPayload.getBytes();

        // Create a Message object with the payload
        Message<byte[]> message = MessageBuilder.withPayload(payload).build();

        // Call handleMessage method
        notificationEventProcessor.handleMessage(message);
    }
}
