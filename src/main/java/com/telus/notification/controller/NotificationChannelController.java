package com.telus.notification.controller;

import com.telus.notification.entity.NotificationChannel;
import com.telus.notification.repository.NotificationChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notifications/channels")
public class NotificationChannelController {

    @Autowired
    private NotificationChannelRepository channelRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createChannel(@RequestBody NotificationChannel channel) {
        channel = channelRepository.save(channel);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("channelId", channel.getId());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getChannels() {
        Map<String, Object> response = new HashMap<>();
        response.put("channels", channelRepository.findByIsActiveTrue());
        return ResponseEntity.ok(response);
    }
}
