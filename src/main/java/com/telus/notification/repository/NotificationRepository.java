package com.telus.notification.repository;

import com.telus.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByExternalUserId(String externalUserId);
    List<Notification> findByExternalUserIdAndType(String externalUserId, String type);
    Optional<Notification> findById(Integer id);
    
    List<Notification> findByExternalUserIdAndIsReadFalse(String externalUserId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.externalUserId = :externalUserId AND n.isRead = false")
    long countUnreadByExternalUserId(String externalUserId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.notificationId = :notificationId")
    int markAsRead(Integer notificationId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.externalUserId = :externalUserId AND n.isRead = false")
    int markAllAsRead(String externalUserId);

    @Query("SELECT n FROM Notification n WHERE n.notificationId = :notificationId")
    Optional<Notification> findByNotificationId(Integer notificationId);
}
