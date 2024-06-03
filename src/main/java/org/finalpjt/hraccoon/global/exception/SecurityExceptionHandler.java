package org.finalpjt.hraccoon.global.exception;

import java.nio.file.AccessDeniedException;

import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class SecurityExceptionHandler {

	/**
	 * 토큰이 유효하지 않을 때 발생하는 예외 처리
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ApiResponse<?>> handleSignatureException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("토큰이 유효하지 않습니다."));
	}

	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ApiResponse<?>> handleMalformedJwtException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("올바르지 않은 토큰입니다."));
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ApiResponse<?>> handleExpiredJwtException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("토큰이 만료되었습니다."));
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.createError(exception.getMessage()));
	}

	/**
	 * 로그인하지 않고 접근할 때 발생하는 예외 처리
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ApiResponse<?>> handleInsufficientAuthenticationException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("로그인이 필요합니다."));
	}
}
