package org.finalpjt.hraccoon.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.finalpjt.hraccoon.domain.notification.data.entity.WebNotification;
import org.finalpjt.hraccoon.domain.notification.repository.EmitterRepository;
import org.finalpjt.hraccoon.domain.notification.repository.WebNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
	private static final Long HEARTBEAT_INTERVAL = 30L * 1000;

	private final EmitterRepository emitterRepository;

	private final WebNotificationRepository webNotificationRepository;

	public SseEmitter subscribe(String userId, String lastEventId) {
		String emitterId = makeTimeIncludeId(userId);

		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
		emitter.onError((Throwable t) -> {
			emitter.complete();
			emitterRepository.deleteById(emitterId);
		});

		/* 503 에러를 방지하기 위한 더미 이벤트 전송 */
		String eventId = makeTimeIncludeId(userId);

		try {
			emitter.send(
				SseEmitter.event().id(eventId).name("sse").data("EventStream Created. [userId = " + userId + "]"));
		} catch (Exception e) {
			log.error("[ERROR] Failed to send dummy event. [emitterId = {}, userId = {}]", emitterId, userId, e);
			emitterRepository.deleteById(emitterId);
		}


		/* 주기적으로 heartbeat 메세지 전송 */
		startHeartbeat(emitter, emitterId, userId);

		/* 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방 */
		if (hasLostData(lastEventId)) {
			sendLostData(lastEventId, userId, emitterId, emitter);
		}

		return emitter;
	}

	private void startHeartbeat(SseEmitter emitter, String emitterId, String userId) {
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			try {
				emitter.send(SseEmitter.event().name("heartbeat").data("heartbeat"));
			} catch (IOException e) {
				log.error("[ERROR] Failed to send heartbeat. [emitterId = {}, userId = {}]", emitterId, userId, e);
				emitterRepository.deleteById(emitterId);
			}
		}, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
	}

	private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(eventId)
				.name("sse")
				.data(data)
			);
		} catch (IOException exception) {
			/* 클라이언트와의 연결이 끊어졌거나 오류 발생한 경우, 해당 연결 종료 및 관련 정보 삭제 */
			log.error("[ERROR] Failed to send notification. [emitterId = {}]", emitterId);
			emitter.completeWithError(exception);
			emitterRepository.deleteById(emitterId);
		}
	}

	private void sendLostData(String lastEventId, String userId, String emitterId, SseEmitter emitter) {
		Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
		eventCaches.entrySet().stream()
			.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
			.forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
	}

	/*
	 * 지정된 수신자에게 알림을 전송하는 메서드
	 * @param recipient 지정된 수신자(사번)
	 * @param message 알림 메세지
	 */
	public void send(String recipient, String sender, String message) {
		WebNotification notification = webNotificationRepository.save(WebNotification.builder()
			.webNotificationRecipient(recipient)
			.webNotificationSender(sender)
			.webNotificationMessage(message)
			.build()
		);

		String eventId = recipient + "_" + System.currentTimeMillis();
		Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(recipient);

		emitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, notification);
				sendNotification(emitter, eventId, key, notification);
			}
		);
	}

	private String makeTimeIncludeId(String userId) {
		return userId + "_" + System.currentTimeMillis();
	}

	private boolean hasLostData(String lastEventId) {
		return !lastEventId.isEmpty();
	}

	public List<WebNotification> getUnreadNotifications(String userId) {
		return webNotificationRepository.findWebNotificationByWebNotificationRecipientAndWebNotificationIsReadIsFalse(userId);
	}

	public void markAsRead(Long webNotificationNo) {
		WebNotification notification = webNotificationRepository.findById(webNotificationNo).orElseThrow();

		notification.read();
		webNotificationRepository.save(notification);
	}
}
