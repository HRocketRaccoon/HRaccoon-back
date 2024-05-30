package org.finalpjt.hraccoon.config;

import java.util.Arrays;
import java.util.Collections;

import org.finalpjt.hraccoon.global.security.JwtAuthenticationFilter;
import org.finalpjt.hraccoon.global.security.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;

	private final AuthenticationEntryPoint entryPoint;

	/* 허용 URL. 인증과 인가를 거치지 않을 경우 사용*/
	private final String[] allowUrls = {
		"/api/v1/user/create",
		"/api/v1/auth/sign-in"
	};

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
			.csrf(CsrfConfigurer<HttpSecurity>::disable) // csrf 비활성화
			.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("*"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(Arrays.asList("Authorization"));
					config.setMaxAge(3600L);

					return config;
				}
			}))
			.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint)) // spring security 에서 인증 실패시 예외처리
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), BasicAuthenticationFilter.class)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers(allowUrls).permitAll()
				// .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 Console 접속은 모두에게 허용
				.anyRequest().authenticated()); // 나머지 요청 인증 필요

		// http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // H2 Console 사용을 위한 설정
		http.formLogin(AbstractHttpConfigurer::disable); // form login 비활성화
		http.httpBasic(Customizer.withDefaults()); // http basic 인증 사용

		return (SecurityFilterChain) http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
