package com.telus.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.dto.SendNotificationRequest;
import com.telus.notification.dto.NotificationTemplate;
import com.telus.notification.service.NotificationService;
import com.telus.notification.service.NotificationTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationTemplateService templateService;

    @Test
    void testSendNotification() throws Exception {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId("test-user");
        request.setType("TEST");
        request.setTemplateId("template-1");
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        request.setData(data);
        request.setChannels(Arrays.asList("email", "in_app"));

        when(notificationService.sendNotification(any(SendNotificationRequest.class)))
            .thenReturn("test-notification-id");

        // Act & Assert
        mockMvc.perform(post("/api/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.notificationId").value("test-notification-id"));
    }

    @Test
    void testGetNotificationTemplates() throws Exception {
        // Arrange
        List<NotificationTemplate> templates = Arrays.asList(
            new NotificationTemplate("1", "Template 1", "EVENT_1", "Subject 1"),
            new NotificationTemplate("2", "Template 2", "EVENT_2", "Subject 2")
        );
        when(templateService.getAllTemplates()).thenReturn(templates);

        // Act & Assert
        mockMvc.perform(get("/api/notifications/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templates").isArray())
                .andExpect(jsonPath("$.templates.length()").value(2))
                .andExpect(jsonPath("$.templates[0].id").value("1"))
                .andExpect(jsonPath("$.templates[0].name").value("Template 1"))
                .andExpect(jsonPath("$.templates[1].id").value("2"))
                .andExpect(jsonPath("$.templates[1].name").value("Template 2"));
    }

    @Test
    void testSendNotification_Error() throws Exception {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        when(notificationService.sendNotification(any(SendNotificationRequest.class)))
            .thenThrow(new RuntimeException("Test error"));

        // Act & Assert
        mockMvc.perform(post("/api/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetNotificationTemplates_Error() throws Exception {
        // Arrange
        when(templateService.getAllTemplates())
            .thenThrow(new RuntimeException("Test error"));

        // Act & Assert
        mockMvc.perform(get("/api/notifications/templates"))
                .andExpect(status().isInternalServerError());
    }
}
