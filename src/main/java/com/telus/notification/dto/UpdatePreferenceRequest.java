package com.telus.notification.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdatePreferenceRequest {
    private List<PreferenceUpdateDTO> preferences;
}
