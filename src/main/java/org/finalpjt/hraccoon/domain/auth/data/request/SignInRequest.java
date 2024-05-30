package org.finalpjt.hraccoon.domain.auth.data.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SignInRequest {

	private final String userId;
	private final String userPassword;


	@Builder
	public SignInRequest(String userId, String userPassword) {
		this.userId = userId;
		this.userPassword = userPassword;
	}
}
