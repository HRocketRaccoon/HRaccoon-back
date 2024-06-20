package org.finalpjt.hraccoon.domain.notification.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.notification.data.entity.WebNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebNotificationRepository extends JpaRepository<WebNotification, Long> {
	List<WebNotification> findWebNotificationByWebNotificationSenderAndWebNotificationIsReadIsFalse(String userId);
}
