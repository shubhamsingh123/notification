package com.telus.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

@Component
public class NotificationTemplates {
    @Autowired
    private TemplateEngine templateEngine;

    public String getUserRegistrationTemplate(Map<String, Object> templateVariables) {
        Context context = new Context();
        templateVariables.forEach(context::setVariable);
        
        return templateEngine.process("email-template", context);
    }

    public String getAccountApprovalTemplate(Map<String, Object> templateVariables) {
        Context context = new Context();
        templateVariables.forEach(context::setVariable);
        
        return templateEngine.process("account-approval", context);
    }

    public String getAccountRejectionTemplate(Map<String, Object> templateVariables) {
        Context context = new Context();
        templateVariables.forEach(context::setVariable);
        
        return templateEngine.process("account-rejected", context);
    }
}
