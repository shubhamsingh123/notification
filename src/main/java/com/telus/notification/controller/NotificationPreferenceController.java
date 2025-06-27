package com.telus.notification.controller;

import com.telus.notification.dto.PreferenceResponse;
import com.telus.notification.dto.UpdatePreferenceRequest;
import com.telus.notification.service.NotificationPreferenceService;
import com.telus.notification.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications/preferences")
public class NotificationPreferenceController {

    @Autowired
    private NotificationPreferenceService preferenceService;

    @GetMapping
    public ResponseEntity<PreferenceResponse> getUserPreferences() {
        String userId = SecurityUtils.getCurrentUserId();
        PreferenceResponse preferences = preferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updatePreferences(@RequestBody UpdatePreferenceRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        preferenceService.updatePreferences(userId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Preferences updated successfully");
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
}
