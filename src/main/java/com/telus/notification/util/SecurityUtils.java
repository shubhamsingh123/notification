package com.telus.notification.util;

import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    
    // TODO: Replace with actual authentication in production
    public static String getCurrentUserId() {
        // Using a mock user ID for testing
        return "test-user-123";
    }
}
