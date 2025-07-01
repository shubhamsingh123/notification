package com.telus.notification.repository;

import com.telus.notification.entity.UserNotificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationDetailsRepository extends JpaRepository<UserNotificationDetails, String> {
}
