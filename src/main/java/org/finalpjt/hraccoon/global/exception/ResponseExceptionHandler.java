package org.finalpjt.hraccoon.global.exception;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice(basePackages = "org.finalpjt.hraccoon")
public class ResponseExceptionHandler {

	/**
	 * IllegalArgumentException, NoSuchElementException 예외 처리
	 * @param e 예외
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
	public ResponseEntity<ApiResponse<?>> handleCommonException(Exception e) {
		return ResponseEntity.badRequest().body(ApiResponse.createError(e.getMessage()));
	}

	/**
	 * AccessDeniedException 예외 처리
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<?>> handleAccessDeniedException() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.createError("접근이 거부되었습니다."));
	}

	/**
	 * DuplicateKeyException 예외 처리
	 * @param exception 예외
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
	}

	/**
	 * MethodArgumentNotValidException 예외 처리
	 * @param bindingResult 유효하지 않은 데이터
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationExceptions(BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
	}

	/**
	 * SignatureException 예외 처리
	 * @param exception 예외
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException exception) {
		return ResponseEntity.status(exception.getStatusCode()).body(ApiResponse.createError(exception.getReason()));
	}

	/**
	 * RuntimeException 예외 처리
	 * @param exception 예외
	 * @return ApiResponse<?>
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<?>> handleException(RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.createError(exception.getMessage()));
	}

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
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.createError("토큰이 만료되었습니다. 다시 로그인해주세요."));
	}
}

