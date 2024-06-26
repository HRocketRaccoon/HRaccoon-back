package org.finalpjt.hraccoon.domain.user.constant;

public class ValidationConstants {
	public static final String MOBILE_PATTERN = "^010\\d{8}$";
	public static final String GENDER_PATTERN = "^(MALE|FEMALE)$";
	public static final String ROLE_PATTERN = "^(ADMIN|USER)$";
	public static final String PASSWORD_PATTERN = "^(?=.*[!@#$%^&*_=])[a-zA-Z0-9!@#$%^&*_=]{8,16}$";

	public static final String PASSWORD_SIZE_MESSAGE = "비밀번호는 8자리 이상 16자리 이하로 구성된 문자열이어야 합니다.";
	public static final String PASSWORD_PATTERN_MESSAGE = "비밀번호는 영문 대문자, 소문자, 숫자, 그리고 최소 하나 이상의 특수문자(!,@,#,$,%,^,&,*,_)를 포함해야 합니다.";
	public static final String MOBILE_MESSAGE = "모바일 번호는 010으로 시작하여 8자리 숫자여야 합니다.";
	public static final String GENDER_MESSAGE = "성별은 MALE 또는 FEMALE 이어야 합니다.";
	public static final String EMAIL_MESSAGE = "이메일 형식이 올바르지 않습니다.";
	public static final String ROLE_MESSAGE = "사용자 권한은 ADMIN 또는 USER 이어야 합니다.";
}
