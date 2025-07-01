package com.telus.notification.service.impl;

import com.telus.notification.entity.NotificationTemplate;
import com.telus.notification.repository.NotificationTemplateRepository;
import com.telus.notification.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public NotificationTemplateServiceImpl(NotificationTemplateRepository notificationTemplateRepository, TemplateEngine templateEngine) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.templateEngine = templateEngine;
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
    public NotificationTemplate getTemplateById(Long id) {
        return notificationTemplateRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTemplate(Long id) {
        notificationTemplateRepository.deleteById(id);
    }

    private String stripWhitespace(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    private void logTemplateDetails(NotificationTemplate template) {
        if (template != null) {
            String originalBody = template.getBodyTemplate();
            String strippedBody = stripWhitespace(originalBody);
            
            logger.info("Template Details:");
            logger.info("- ID: {}", template.getId());
            logger.info("- Event Type: {}", template.getEventType());
            logger.info("- Original Body Length: {}", originalBody.length());
            logger.info("- Stripped Body Length: {}", strippedBody.length());
            logger.info("- Original Body: '{}'", originalBody);
            logger.info("- Stripped Body: '{}'", strippedBody);
            
            // Check for potential issues
            if (originalBody.length() != strippedBody.length()) {
                logger.warn("Template body contains extra whitespace or hidden characters");
            }
            
            // Check placeholder format
            String[] placeholders = {"{{username}}", "{{role}}", "{{email}}", "{{status}}"};
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
            }
            return template;
        } catch (Exception e) {
            logger.error("Error finding template for event type: {}", eventType, e);
            return null;
        }
    }

    @Override
    public NotificationTemplate updateTemplateBody(String eventType, String newTemplate) {
        logger.info("Updating template body for event type: {}", eventType);
        logger.info("New template content: '{}'", newTemplate);
        
        NotificationTemplate template = getTemplateByEventType(eventType);
        if (template == null) {
            logger.error("No template found for event type: {}", eventType);
            throw new RuntimeException("Template not found for event type: " + eventType);
        }
        
        template.setBodyTemplate(newTemplate);
        template.setUpdatedAt(LocalDateTime.now());
        
        NotificationTemplate updatedTemplate = notificationTemplateRepository.save(template);
        logger.info("Template updated successfully for event type: {}", eventType);
        logTemplateDetails(updatedTemplate);
        
        return updatedTemplate;
    }

    @Override
    public String processTemplate(String template, Map<String, Object> variables) {
        if (template == null) {
            logger.error("Template is null");
            return "Template processing error: template is null";
        }
        
        logger.info("Starting Thymeleaf template processing");
        logger.info("Original template: '{}'", template);
        logger.info("Variables to process: {}", variables);
        
        Context context = new Context();
        variables.forEach((key, value) -> {
            context.setVariable(key, value);
            logger.info("Setting variable '{}' with value '{}'", key, value);
        });
        
        String processed = templateEngine.process(template, context);
        
        logger.info("Final processed template: '{}'", processed);
        return processed;
    }
}
