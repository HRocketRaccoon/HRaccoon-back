package org.finalpjt.hraccoon.global.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisDao {

	@Value("${spring.data.redis.host}")
	private String host;

	private final RedisTemplate<String, String> redisTemplate;

	public RedisDao(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setValues(String key, String data) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data);
	}

	public void setValues(String key, String data, Duration duration) {
		log.info("::::::: redis dao for refresh ::::::::");
		log.info("::::::: host :::::::: {}", host);
		log.info("::::::: key :::::::: {}", key);
		log.info(":::::::::::: redisTemplate :::::::: {}", redisTemplate);
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		log.info(";::::::: values setup :::::::: {}", values);
		values.set(key, data, duration);
		log.info("::::: set values ::::::::");
	}

	public String getValues(String key) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(key);
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}
}
