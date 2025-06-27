package com.telus.notification.dto;

import lombok.Data;

@Data
public class PreferenceDTO {
    private String channelId;
    private String channelName;
    private String channelType;
    private String eventType;
    private boolean isEnabled;
}
