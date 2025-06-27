package com.telus.notification.dto;

import lombok.Data;
import java.util.List;

@Data
public class PreferenceResponse {
    private List<PreferenceDTO> preferences;
}
