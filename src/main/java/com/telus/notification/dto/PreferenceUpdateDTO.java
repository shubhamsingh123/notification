package com.telus.notification.dto;

import lombok.Data;

@Data
public class PreferenceUpdateDTO {
    private String channelId;
    private String eventType;
    private boolean isEnabled;
}
