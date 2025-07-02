package com.telus.notification.service.impl;

import com.telus.notification.entity.Notification;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getUnreadNotificationsByExternalUserId(String externalUserId) {
        return notificationRepository.findByExternalUserIdAndIsReadFalse(externalUserId);
    }

    @Override
    public void markNotificationAsRead(Integer notificationId) {
        notificationRepository.findByNotificationId(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        
        int updated = notificationRepository.markAsRead(notificationId);
        if (updated == 0) {
            throw new RuntimeException("Failed to mark notification as read");
        }
    }
}
