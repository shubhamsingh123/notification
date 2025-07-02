package com.telus.notification.service;

import com.telus.notification.entity.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getUnreadNotificationsByExternalUserId(String externalUserId);
    void markNotificationAsRead(Integer notificationId);
}
