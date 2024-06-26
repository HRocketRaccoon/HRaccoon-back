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
public class WebNotification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "web_notification_no")
	private Long webNotificationNo;

	@Column(name = "web_notification_recipient", nullable = false)
	private String webNotificationRecipient;

	@Column(name = "web_notification_sender", nullable = false)
	private String webNotificationSender;

	@Column(name = "web_notification_message", nullable = false, columnDefinition = "TEXT")
	private String webNotificationMessage;

	@Column(name = "web_notification_is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean webNotificationIsRead;

	@Column(name = "web_notification_send_at", nullable = false)
	private LocalDateTime webNotificationSendAt;

	@Builder
	public WebNotification(String webNotificationRecipient, String webNotificationSender, String webNotificationMessage) {
		this.webNotificationRecipient = webNotificationRecipient;
		this.webNotificationSender = webNotificationSender;
		this.webNotificationMessage = webNotificationMessage;
		this.webNotificationSendAt = LocalDateTime.now();
		this.webNotificationIsRead = false;
	}

	public void read() {
		this.webNotificationIsRead = true;
	}
}
