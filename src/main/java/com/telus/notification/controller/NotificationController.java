package com.telus.notification.controller;

import com.telus.notification.model.BaseEvent;
import com.telus.notification.entity.Notification;
import com.telus.notification.service.NotificationEventProcessor;
import com.telus.notification.repository.NotificationRepository;
import com.telus.notification.exception.NotificationException;
import com.telus.notification.util.SecurityUtils;
import com.telus.notification.annotation.ServiceAuth;
import com.telus.notification.dto.SendNotificationRequest;
import com.telus.notification.dto.NotificationTemplate;
import com.telus.notification.dto.TemplateResponse;
import com.telus.notification.service.NotificationService;
import com.telus.notification.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationEventProcessor eventProcessor;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationTemplateService templateService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserNotifications(@RequestParam(required = false) String userId) {
        try {
            // If no userId provided, fall back to SecurityUtils
            if (userId == null) {
                userId = SecurityUtils.getCurrentUserId();
            }
            logger.info("Fetching notifications for user: {}", userId);
            List<Notification> notifications = notificationRepository.findByExternalUserId(userId);
            long unreadCount = notificationRepository.countUnreadByExternalUserId(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("notifications", notifications);
            response.put("unreadCount", unreadCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching notifications: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody Map<String, Object> request) {
        try {
            logger.info("Creating new notification");
            String recipientId = (String) request.get("recipientId");
            String message = (String) request.get("message");
            String type = (String) request.get("type");
            
            if (recipientId == null || message == null || type == null) {
                throw new NotificationException("recipientId, message, and type are required fields");
            }
            
            Notification notification = new Notification();
            notification.setExternalUserId(recipientId);
            notification.setMessage(message);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setNotificationId(java.util.UUID.randomUUID().toString());
            
            notification = notificationRepository.save(notification);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("notificationId", notification.getNotificationId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating notification: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(@PathVariable String notificationId) {
        try {
            logger.info("Marking notification as read: {}", notificationId);
            int updatedCount = notificationRepository.markAsRead(notificationId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", updatedCount > 0);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead() {
        try {
            String userId = SecurityUtils.getCurrentUserId();
            logger.info("Marking all notifications as read for user: {}", userId);
            int updatedCount = notificationRepository.markAllAsRead(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", updatedCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error marking all notifications as read: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @ServiceAuth
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody SendNotificationRequest request) {
        try {
            logger.info("Sending notification for user: {}", request.getUserId());
            String notificationId = notificationService.sendNotification(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("notificationId", notificationId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @ServiceAuth
    @GetMapping("/templates")
    public ResponseEntity<TemplateResponse> getNotificationTemplates() {
        try {
            logger.info("Fetching notification templates");
            List<NotificationTemplate> templates = templateService.getAllTemplates();
            return ResponseEntity.ok(new TemplateResponse(templates));
        } catch (Exception e) {
            logger.error("Error fetching notification templates: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

@PostMapping("/events")
public ResponseEntity<String> handleEvent(@RequestBody BaseEvent event) {
    try {
        if (!"UserRegistered".equals(event.getEventType())) {
            logger.warn("Received unsupported event type: {}", event.getEventType());
            return ResponseEntity.badRequest().body("Unsupported event type");
        }
        logger.info("Received UserRegistered event");
        eventProcessor.processEvent(event);
        return ResponseEntity.ok("UserRegistered event processed successfully");
    } catch (Exception e) {
        logger.error("Error processing UserRegistered event: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body("Error processing UserRegistered event: " + e.getMessage());
    }
}
}
