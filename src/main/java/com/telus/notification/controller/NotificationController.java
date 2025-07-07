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
import com.telus.notification.model.UserRegistrationEmailModel;

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
            return ResponseEntity.ok(new ApiResponse<>(true, "Notification processed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null, e.getMessage()));
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
