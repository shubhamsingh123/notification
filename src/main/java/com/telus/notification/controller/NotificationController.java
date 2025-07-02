package com.telus.notification.controller;

import com.telus.notification.dto.ApiResponse;
import com.telus.notification.entity.Notification;
import com.telus.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread/{externalUserId}")
    public ResponseEntity<ApiResponse> getUnreadNotifications(@PathVariable String externalUserId) {
        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByExternalUserId(externalUserId);
        return ResponseEntity.ok(ApiResponse.success(unreadNotifications));
    }
}
