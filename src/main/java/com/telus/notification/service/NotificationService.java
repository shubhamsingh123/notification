package com.telus.notification.service;

import com.telus.notification.dto.SendNotificationRequest;

public interface NotificationService {
    String sendNotification(SendNotificationRequest request);
}
