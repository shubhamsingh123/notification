package com.telus.notification.service;

import com.telus.notification.entity.NotificationTemplate;
import java.util.List;
import java.util.Map;

public interface NotificationTemplateService {
    List<NotificationTemplate> getAllTemplates();
    NotificationTemplate saveTemplate(NotificationTemplate template);
    NotificationTemplate getTemplateById(Long id);
    void deleteTemplate(Long id);
    
    /**
     * Find a template by its event type
     * @param eventType the type of event
     * @return the template for the given event type
     */
    NotificationTemplate getTemplateByEventType(String eventType);
    
    /**
     * Process a template by replacing placeholders with actual values
     * @param template the template string containing placeholders
     * @param variables map of variable names to their values
     * @return the processed template with all variables replaced using Thymeleaf
     */
    String processTemplate(String template, Map<String, Object> variables);

    /**
     * Update the body template for a specific event type
     * @param eventType the type of event
     * @param newTemplate the new template content
     * @return the updated template
     */
    NotificationTemplate updateTemplateBody(String eventType, String newTemplate);
}
