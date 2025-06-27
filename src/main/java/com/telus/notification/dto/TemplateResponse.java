package com.telus.notification.dto;

import java.util.List;

public class TemplateResponse {
    private List<NotificationTemplate> templates;

    public TemplateResponse(List<NotificationTemplate> templates) {
        this.templates = templates;
    }

    public List<NotificationTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<NotificationTemplate> templates) {
        this.templates = templates;
    }
}
