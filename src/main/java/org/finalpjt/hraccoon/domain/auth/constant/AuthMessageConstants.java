package org.finalpjt.hraccoon.domain.auth.constant;

public class AuthMessageConstants {
	public static final String AUTH_SUCCESS = "로그인에 성공했습니다.";
	public static final String AUTH_FAIL_USER_NOT_FOUND = "존재하지 않는 사용자입니다.";
	public static final String AUTH_FAIL_PASSWORD_NOT_MATCH = "비밀번호가 일치하지 않습니다.";

	public static final String AUTH_FAIL_TOKEN_CREATE = "토큰 생성에 실패했습니다.";
	public static final String AUTH_TOKEN_REFRESH = "토큰 재발급에 성공했습니다.";
	public static final String AUTH_TOKEN_REFRESH_FAIL = "토큰 재발급에 실패했습니다.";

	public static final String AUTH_LOGOUT = "로그아웃에 성공했습니다.";
	public static final String AUTH_LOGOUT_FAIL = "로그아웃에 실패했습니다.";
}
