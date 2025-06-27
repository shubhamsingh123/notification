package com.telus.notification.controller;

import com.telus.notification.dto.SendNotificationRequest;
import com.telus.notification.dto.NotificationTemplate;
import com.telus.notification.dto.TemplateResponse;
import com.telus.notification.service.NotificationService;
import com.telus.notification.service.NotificationTemplateService;
import com.telus.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationTemplateService templateService;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    void testSendNotification() {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId("test-user");
        request.setType("TEST");
        request.setTemplateId("template-1");
        
        when(notificationService.sendNotification(any(SendNotificationRequest.class)))
            .thenReturn("test-notification-id");

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.sendNotification(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("test-notification-id", response.getBody().get("notificationId"));
    }

    @Test
    void testGetNotificationTemplates() {
        // Arrange
        List<NotificationTemplate> templates = Arrays.asList(
            new NotificationTemplate("1", "Template 1", "EVENT_1", "Subject 1"),
            new NotificationTemplate("2", "Template 2", "EVENT_2", "Subject 2")
        );
        when(templateService.getAllTemplates()).thenReturn(templates);

        // Act
        ResponseEntity<TemplateResponse> response = notificationController.getNotificationTemplates();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTemplates().size());
        assertEquals("Template 1", response.getBody().getTemplates().get(0).getName());
        assertEquals("Template 2", response.getBody().getTemplates().get(1).getName());
    }

    @Test
    void testSendNotification_Error() {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        when(notificationService.sendNotification(any(SendNotificationRequest.class)))
            .thenThrow(new RuntimeException("Test error"));

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.sendNotification(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetNotificationTemplates_Error() {
        // Arrange
        when(templateService.getAllTemplates())
            .thenThrow(new RuntimeException("Test error"));

        // Act
        ResponseEntity<TemplateResponse> response = notificationController.getNotificationTemplates();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
