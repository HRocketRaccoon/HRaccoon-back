package org.finalpjt.hraccoon.domain.auth.service;

import org.finalpjt.hraccoon.domain.auth.data.PayLoad;
import org.finalpjt.hraccoon.domain.auth.data.request.SignInRequest;
import org.finalpjt.hraccoon.domain.auth.data.response.SignInResponse;
import org.finalpjt.hraccoon.global.security.JwtProvider;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final JwtProvider jwtProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public SignInResponse signIn(SignInRequest params) {
		User user = userRepository.findByUserId(params.getUserId())
			.orElseThrow(() -> new BadCredentialsException("존재하지 않는 사용자입니다."));

		if (!passwordEncoder.matches(params.getUserPassword(), user.getUserPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
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

		String atkToken = jwtProvider.createToken(accessTokenPayLoad);
		String rtkToken = jwtProvider.createToken(refreshTokenPayLoad);

		return SignInResponse.builder()
			.accessToken(atkToken)
			.refreshToken(rtkToken)
			.build();
	}
}
