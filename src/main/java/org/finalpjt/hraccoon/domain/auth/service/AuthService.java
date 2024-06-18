package org.finalpjt.hraccoon.domain.auth.service;

import org.finalpjt.hraccoon.domain.auth.constant.AuthMessageConstants;
import org.finalpjt.hraccoon.domain.auth.data.PayLoad;
import org.finalpjt.hraccoon.domain.auth.data.request.SignInRequest;
import org.finalpjt.hraccoon.domain.auth.data.response.TokenResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.global.security.JwtProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final JwtProvider jwtProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public TokenResponse signIn(SignInRequest params) {
		User user = userRepository.findByUserId(params.getUserId())
			.orElseThrow(() -> new BadCredentialsException(AuthMessageConstants.AUTH_FAIL_USER_NOT_FOUND));

		if (!passwordEncoder.matches(params.getUserPassword(), user.getUserPassword())) {
			throw new BadCredentialsException(AuthMessageConstants.AUTH_FAIL_PASSWORD_NOT_MATCH);
		}

		PayLoad accessTokenPayLoad = PayLoad.builder()
			.userNo(user.getUserNo())
			.userId(user.getUserId())
			.type("ATK")
			.authority(user.getUserRole().toString())
			.build();

		PayLoad refreshTokenPayLoad = PayLoad.builder()
			.userNo(user.getUserNo())
			.userId(user.getUserId())
			.type("RTK")
			.authority(user.getUserRole().toString())
			.build();

		try {
			String atkToken = jwtProvider.createToken(accessTokenPayLoad);
			String rtkToken = jwtProvider.createToken(refreshTokenPayLoad);

			return TokenResponse.builder()
				.accessToken(atkToken)
				.refreshToken(rtkToken)
				.build();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(AuthMessageConstants.AUTH_FAIL_TOKEN_CREATE);
		}
	}

	@Transactional
	public TokenResponse reIssuance(String refreshToken) {
		try {
			return jwtProvider.reIssuanceTokens(refreshToken);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(AuthMessageConstants.AUTH_TOKEN_REFRESH_FAIL);
		}
	}

	@Transactional
	public void signOut(String refreshToken) {
		try {
			jwtProvider.deleteToken(refreshToken);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(AuthMessageConstants.AUTH_LOGOUT_FAIL);
		}
	}
}
