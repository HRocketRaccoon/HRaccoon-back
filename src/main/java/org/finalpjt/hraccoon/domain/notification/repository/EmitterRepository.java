package org.finalpjt.hraccoon.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmitterRepository {

	public final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
		sseEmitterMap.put(emitterId, sseEmitter);
		return sseEmitter;
	}

	public void saveEventCache(String eventCacheId, Object event) {
		eventCache.put(eventCacheId, event);
	}

	public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId) {
		return sseEmitterMap.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(userId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, Object> findAllEventCacheStartWithByUserId(String userId) {
		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(userId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void deleteById(String id) {
		sseEmitterMap.remove(id);
	}

	public void deleteAllEmitterStartWithId(String userId) {
		sseEmitterMap.forEach(
			(key, emitter) -> {
				if (key.startsWith(userId)) {
					sseEmitterMap.remove(key);
				}
			}
		);
	}

	public void deleteAllEventCacheStartWithId(String userId) {
		eventCache.forEach(
			(key, emitter) -> {
				if (key.startsWith(userId)) {
					eventCache.remove(key);
				}
			}
		);
	}

	// public void put(String key, SseEmitter sseEmitter) {
	// 	sseEmitterMap.put(key, sseEmitter);
	// }
	//
	// public Optional<SseEmitter> get(String key) {
	// 	return Optional.ofNullable(sseEmitterMap.get(key));
	// }
	//
	// public void deleteById(String key) {
	// 	sseEmitterMap.remove(key);
	// }
}
