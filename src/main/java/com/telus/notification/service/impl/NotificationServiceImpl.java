package com.telus.notification.service.impl;

import com.telus.notification.dto.SendNotificationRequest;
import com.telus.notification.service.NotificationService;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public String sendNotification(SendNotificationRequest request) {
        Notification notification = new Notification();
        notification.setExternalUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setNotificationId(UUID.randomUUID().toString());
        notification.setIsRead(false);
        
        // Convert template data to message
        String message = "Notification from template: " + request.getTemplateId();
        if (request.getData() != null && !request.getData().isEmpty()) {
            message += " with data: " + request.getData().toString();
        }
        notification.setMessage(message);
        
        notification = notificationRepository.save(notification);
        return notification.getNotificationId();
    }
}
