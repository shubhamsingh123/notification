package com.telus.notification.repository;

import com.telus.notification.entity.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    List<NotificationPreference> findByExternalUserId(String externalUserId);
    Optional<NotificationPreference> findByExternalUserIdAndChannelIdAndEventType(String externalUserId, Long channelId, String eventType);
    List<NotificationPreference> findByExternalUserIdAndEventType(String externalUserId, String eventType);
}
