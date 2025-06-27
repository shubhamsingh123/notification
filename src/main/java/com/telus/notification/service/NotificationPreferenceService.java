package com.telus.notification.service;

import com.telus.notification.dto.PreferenceDTO;
import com.telus.notification.dto.PreferenceResponse;
import com.telus.notification.dto.UpdatePreferenceRequest;
import com.telus.notification.entity.NotificationChannel;
import com.telus.notification.entity.NotificationPreference;
import com.telus.notification.exception.NotificationException;
import com.telus.notification.repository.NotificationChannelRepository;
import com.telus.notification.repository.NotificationPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationPreferenceService {

    @Autowired
    private NotificationPreferenceRepository preferenceRepository;

    @Autowired
    private NotificationChannelRepository channelRepository;

    public PreferenceResponse getUserPreferences(String userId) {
        List<NotificationPreference> preferences = preferenceRepository.findByExternalUserId(userId);
        
        List<PreferenceDTO> preferenceDTOs = preferences.stream()
            .map(pref -> {
                PreferenceDTO dto = new PreferenceDTO();
                dto.setChannelId(String.valueOf(pref.getChannel().getId()));
                dto.setChannelName(pref.getChannel().getName());
                dto.setChannelType(pref.getChannel().getType());
                dto.setEventType(pref.getEventType());
                dto.setEnabled(pref.getIsEnabled());
                return dto;
            })
            .collect(Collectors.toList());

        PreferenceResponse response = new PreferenceResponse();
        response.setPreferences(preferenceDTOs);
        return response;
    }

    @Transactional
    public void updatePreferences(String userId, UpdatePreferenceRequest request) {
        request.getPreferences().forEach(prefUpdate -> {
            NotificationChannel channel = channelRepository.findById(Long.parseLong(prefUpdate.getChannelId()))
                .orElseThrow(() -> new NotificationException("Channel not found: " + prefUpdate.getChannelId()));

            NotificationPreference preference = preferenceRepository
                .findByExternalUserIdAndChannelIdAndEventType(userId, channel.getId(), prefUpdate.getEventType())
                .orElseGet(() -> {
                    NotificationPreference newPref = new NotificationPreference();
                    newPref.setPreferenceId(UUID.randomUUID().toString());
                    newPref.setExternalUserId(userId);
                    newPref.setChannel(channel);
                    newPref.setEventType(prefUpdate.getEventType());
                    return newPref;
                });

            preference.setIsEnabled(prefUpdate.isEnabled());
            preferenceRepository.save(preference);
        });
    }
}
