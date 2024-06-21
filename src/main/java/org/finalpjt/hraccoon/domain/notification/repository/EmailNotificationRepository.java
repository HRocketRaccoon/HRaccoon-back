package org.finalpjt.hraccoon.domain.notification.repository;

import org.finalpjt.hraccoon.domain.notification.data.entity.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
}
