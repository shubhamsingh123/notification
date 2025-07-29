package com.telus.notification.controller;

import com.telus.notification.dto.ApiResponse;
import com.telus.notification.entity.Notification;
import com.telus.notification.service.NotificationService;
import com.telus.notification.model.BaseEvent;
import com.telus.notification.service.NotificationEventProcessor;
import com.telus.notification.service.EmailService;
import com.telus.notification.service.NotificationTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.time.Year;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.telus.notification.model.UserRegistrationEmailModel;
import java.time.Instant;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationEventProcessor notificationEventProcessor;
    private final EmailService emailService;
    private final NotificationTemplates notificationTemplates;

    @Autowired
    public NotificationController(
            NotificationService notificationService,
            NotificationEventProcessor notificationEventProcessor,
            EmailService emailService,
            NotificationTemplates notificationTemplates) {
        this.notificationService = notificationService;
        this.notificationEventProcessor = notificationEventProcessor;
        this.emailService = emailService;
        this.notificationTemplates = notificationTemplates;
        
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<String>> processNotification(@RequestBody BaseEvent event) {
        try {
            notificationEventProcessor.processEvent(event);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Notification processed successfully",
                "Operation successful",
                200,
                null,
                Instant.now().toEpochMilli()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                false,
                null,
                "Operation failed",
                400,
                e.getMessage(),
                Instant.now().toEpochMilli()
            ));
        }
    }

    // @PostMapping("/test-template-email")
    // public ResponseEntity<Map<String, String>> sendTestTemplateEmail(@RequestBody Map<String, Object> requestBody) {
    //     try {
    //         String to = (String) requestBody.get("to");
    //         String subject = (String) requestBody.get("subject");
    //         String userName = (String) requestBody.get("userName");
    //         String userId = (String) requestBody.get("userId");
    //         String loginUrl = (String) requestBody.get("loginUrl");
            
    //         if (to == null || subject == null || userName == null || userId == null || loginUrl == null) {
    //             Map<String, String> response = new HashMap<>();
    //             response.put("status", "error");
    //             response.put("message", "Missing required fields: to, subject, userName, userId, loginUrl");
    //             return ResponseEntity.badRequest().body(response);
    //         }
            
    //         UserRegistrationEmailModel model = new UserRegistrationEmailModel();
    //         model.setUserName(userName);
    //         model.setUserEmail(userId);
    //         model.setLoginUrl(loginUrl);
    //         model.setRegistrationDate(LocalDateTime.now());
    //         model.setManagerEmail(userId); // Setting manager email same as user email for testing
            
    //         emailService.sendUserRegistrationEmail(model, to);
            
    //         Map<String, String> response = new HashMap<>();
    //         response.put("status", "success");
    //         response.put("message", "Template email sent to " + to);
            
    //         return ResponseEntity.ok(response);
    //     } catch (Exception e) {
    //         Map<String, String> response = new HashMap<>();
    //         response.put("status", "error");
    //         response.put("message", e.getMessage());
            
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    //     }
    // }

    @GetMapping("/unread/{externalUserId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotifications(@PathVariable String externalUserId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByExternalUserId(externalUserId);
            
            List<Map<String, Object>> notificationList = notifications.stream()
                .map(notification -> {
                    Map<String, Object> notificationMap = new HashMap<>();
                    notificationMap.put("id", notification.getNotificationId());
                    notificationMap.put("message", notification.getMessage());
                    notificationMap.put("created_at", notification.getCreatedAt());
                    notificationMap.put("is_read", notification.getIsRead());
                    return notificationMap;
                })
                .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Notifications loaded successfully");
            responseData.put("count", notifications.size());
            responseData.put("notifications", notificationList);

            ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(
                true,
                responseData,
                "Notifications loaded successfully",
                200,
                null,
                Instant.now().toEpochMilli()
            );

            return ResponseEntity.ok(apiResponse);
        } catch (RuntimeException e) {
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>(
                false,
                null,
                "Error loading notifications",
                400,
                e.getMessage(),
                Instant.now().toEpochMilli()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @PutMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<ApiResponse<String>> markNotificationAsRead(@PathVariable Integer notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(new ApiResponse<>(
                true, 
                "Notification marked as read successfully",
                "Operation successful",
                200,
                null,
                Instant.now().toEpochMilli()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                false,
                null,
                "Operation failed",
                400,
                e.getMessage(),
                Instant.now().toEpochMilli()
                
            ));
        }
    }
}
