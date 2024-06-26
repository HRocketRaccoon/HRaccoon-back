package org.finalpjt.hraccoon.domain.notification.ctrl;

import java.util.List;

import org.finalpjt.hraccoon.domain.notification.data.entity.WebNotification;
import org.finalpjt.hraccoon.domain.notification.service.NotificationService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(@AuthenticationPrincipal String userId,
		@RequestHeader(value = "LAST-EVENT-ID", required = false, defaultValue = "") String lastEventId) {
		log.info("userId : {}", userId);

		return notificationService.subscribe(userId, lastEventId);
	}

	@GetMapping("/unread")
	public ApiResponse<List<WebNotification>> getUnreadNotifications(@RequestParam("userId") String userId) {
		return ApiResponse.createSuccess(notificationService.getUnreadNotifications(userId));
	}

	@PostMapping("/read/{webNotificationNo}")
	public ApiResponse<?> readNotification(@PathVariable Long webNotificationNo) {
		notificationService.markAsRead(webNotificationNo);

		return ApiResponse.createSuccessWithNoContent();
	}
}
