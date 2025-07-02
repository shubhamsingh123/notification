package com.telus.notification.controller;

import com.telus.notification.dto.ApiResponse;
import com.telus.notification.entity.Notification;
import com.telus.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(@PathVariable String externalUserId) {
        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByExternalUserId(externalUserId);
            return ResponseEntity.ok(new ApiResponse<>(true, unreadNotifications, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

     @PutMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<ApiResponse<String>> markNotificationAsRead(@PathVariable Integer notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }
}
