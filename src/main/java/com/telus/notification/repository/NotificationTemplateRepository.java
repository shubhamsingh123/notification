package com.telus.notification.repository;

import com.telus.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer> {
    /**
     * Find a template by its event type
     * @param eventType the type of event
     * @return the template for the given event type, or null if not found
     */
    @Query(value = "SELECT * FROM notification_templates WHERE event_type = :eventType", nativeQuery = true)
    NotificationTemplate findByEventType(@Param("eventType") String eventType);
}
