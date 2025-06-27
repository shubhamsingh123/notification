package com.telus.notification.service;

import com.telus.notification.dto.NotificationTemplate;
import java.util.List;

public interface NotificationTemplateService {
    List<NotificationTemplate> getAllTemplates();
}
