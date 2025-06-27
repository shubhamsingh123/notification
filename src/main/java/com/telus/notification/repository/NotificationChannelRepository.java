package com.telus.notification.repository;

import com.telus.notification.entity.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {
    List<NotificationChannel> findByIsActiveTrue();
    List<NotificationChannel> findByTypeAndIsActiveTrue(String type);
}
