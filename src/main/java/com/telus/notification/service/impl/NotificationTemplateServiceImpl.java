package com.telus.notification.service.impl;

import com.telus.notification.dto.NotificationTemplate;
import com.telus.notification.service.NotificationTemplateService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    @Override
    public List<NotificationTemplate> getAllTemplates() {
        // For demonstration, returning hardcoded templates
        // In a real implementation, this would fetch from a database
        return Arrays.asList(
            new NotificationTemplate("1", "Welcome Email", "WELCOME", "Welcome to our service"),
            new NotificationTemplate("2", "Order Confirmation", "ORDER", "Your order has been confirmed"),
            new NotificationTemplate("3", "Password Reset", "SECURITY", "Reset your password")
        );
    }
}
