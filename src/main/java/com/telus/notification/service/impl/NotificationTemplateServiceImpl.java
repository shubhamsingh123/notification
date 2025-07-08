package com.telus.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.notification.entity.NotificationTemplate;
import com.telus.notification.repository.NotificationTemplateRepository;
import com.telus.notification.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;


@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTemplateServiceImpl.class);
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final TemplateEngine templateEngine;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationTemplateServiceImpl(NotificationTemplateRepository notificationTemplateRepository, 
                                         TemplateEngine templateEngine,
                                         ObjectMapper objectMapper) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.templateEngine = templateEngine;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<NotificationTemplate> getAllTemplates() {
        return notificationTemplateRepository.findAll();
    }

    @Override
    public NotificationTemplate saveTemplate(NotificationTemplate template) {
        return notificationTemplateRepository.save(template);
    }

    @Override
    public NotificationTemplate getTemplateById(Integer id) {
        return notificationTemplateRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTemplate(Integer id) {
        notificationTemplateRepository.deleteById(id);
    }

    private String stripWhitespace(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    private void logTemplateDetails(NotificationTemplate template) {
        if (template != null) {
            String originalBody = template.getBodyTemplate();
            
            logger.info("Template Details:");
            logger.info("- Template ID: {}", template.getTemplateId());
            logger.info("- Event Type: {}", template.getEventType());
            logger.info("- Subject: {}", template.getSubjectTmp());
            logger.info("- Original Body Length: {}", originalBody.length());
            
            // Check for required placeholders
            String[] placeholders = {"${userName}", "${userId}", "${loginUrl}", "${currentYear}"};
            for (String placeholder : placeholders) {
                if (!originalBody.contains(placeholder)) {
                    logger.warn("Template is missing placeholder: {}", placeholder);
                }
            }
        } else {
            logger.warn("Template is null");
        }
    }

    @Override
    public NotificationTemplate getTemplateByEventType(String eventType) {
        logger.info("Looking for template with event type: {}", eventType);
        try {
            logger.info("Executing findByEventType query for eventType: '{}'", eventType);
            NotificationTemplate template = notificationTemplateRepository.findByEventType(eventType);
            
            if (template != null) {
                logger.info("Template found successfully");
                logTemplateDetails(template);
            } else {
                logger.warn("No template found for event type: {}", eventType);
                // Add debug logging to check all templates
                List<NotificationTemplate> allTemplates = notificationTemplateRepository.findAll();
                logger.debug("All templates in the database:");
                for (NotificationTemplate t : allTemplates) {
                    logger.debug("Template ID: {}, Event Type: {}", t.getTemplateId(), t.getEventType());
                }
            }
            return template;
        } catch (Exception e) {
            logger.error("Error finding template for event type: {}", eventType, e);
            return null;
        }
    }

    @Override
    public NotificationTemplate updateTemplateBody(String eventType, Map<String, Object> newData) {
        logger.info("Updating template body for event type: {}", eventType);
        logger.info("New template data: '{}'", newData);
        
        NotificationTemplate template = getTemplateByEventType(eventType);
        if (template == null) {
            logger.error("No template found for event type: {}", eventType);
            throw new RuntimeException("Template not found for event type: " + eventType);
        }
        
        try {
            String newTemplateBody = objectMapper.writeValueAsString(newData);
            template.setBodyTemplate(newTemplateBody);
            template.setUpdatedAt(LocalDateTime.now());
            template.setUpdatedBy("system");
            
            NotificationTemplate updatedTemplate = notificationTemplateRepository.save(template);
            logger.info("Template updated successfully for event type: {}", eventType);
            logTemplateDetails(updatedTemplate);
            
            return updatedTemplate;
        } catch (Exception e) {
            logger.error("Error updating template for event type: {}", eventType, e);
            throw new RuntimeException("Error updating template: " + e.getMessage());
        }
    }

    @Override
    public String processTemplate(String template, Map<String, Object> variables) {
        if (template == null) {
            logger.error("Template is null");
            return "Template processing error: template is null";
        }
        
        logger.info("Starting template processing");
        logger.debug("Original template length: {}", template.length());
        logger.debug("Variables to process: {}", variables);
        
        String processed = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            processed = processed.replace(placeholder, value);
            logger.debug("Replacing {} with {}", placeholder, value);
        }
        
        logger.debug("Final processed template length: {}", processed.length());
        return processed;
    }
}
