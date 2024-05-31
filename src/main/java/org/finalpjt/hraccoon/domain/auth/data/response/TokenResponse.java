package org.finalpjt.hraccoon.domain.auth.data.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TokenResponse {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	public TokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
