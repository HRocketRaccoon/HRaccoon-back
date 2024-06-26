package org.finalpjt.hraccoon.domain.notification.data.entity;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.global.abstracts.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class EmailNotification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_notification_id")
	private Long emailNotificationId;

	@Column(name = "email_notification_recipient", nullable = false)
	private String emailNotificationRecipient;

	@Column(name = "email_notification_sender", nullable = false)
	private String emailNotificationSender;

	@Column(name = "email_notification_address", nullable = false)
	private String emailNotificationAddress;

	@Column(name = "email_notification_subject", nullable = false)
	private String emailNotificationSubject;

	@Column(name = "email_notification_content", nullable = false, columnDefinition = "TEXT")
	private String emailNotificationContent;

	@Column(name = "email_notification_send_at", nullable = false)
	private LocalDateTime emailNotificationSendAt;

	@Builder
	public EmailNotification(String emailNotificationRecipient, String emailNotificationSender,
		String emailNotificationAddress, String emailNotificationSubject,
		String emailNotificationContent, LocalDateTime emailNotificationSendAt) {
		this.emailNotificationRecipient = emailNotificationRecipient;
		this.emailNotificationSender = emailNotificationSender;
		this.emailNotificationAddress = emailNotificationAddress;
		this.emailNotificationSubject = emailNotificationSubject;
		this.emailNotificationContent = emailNotificationContent;
		this.emailNotificationSendAt = emailNotificationSendAt;
	}
}
