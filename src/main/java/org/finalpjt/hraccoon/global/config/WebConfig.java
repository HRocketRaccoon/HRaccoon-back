package org.finalpjt.hraccoon.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			// 클라이언트 주소 설정
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			// .allowedHeaders("*")
			// .allowCredentials(true)
			.maxAge(3600)
			.exposedHeaders(HttpHeaders.LOCATION); // 필요한 경우 노출할 헤더 추가
	}

}
