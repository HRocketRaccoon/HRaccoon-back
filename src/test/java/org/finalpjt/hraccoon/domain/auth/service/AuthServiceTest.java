package org.finalpjt.hraccoon.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.auth.constant.AuthMessageConstants;

import org.finalpjt.hraccoon.domain.auth.data.request.SignInRequest;
import org.finalpjt.hraccoon.domain.auth.data.response.TokenResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AuthServiceTest {

	@Autowired
	private AuthService authService;
	@Autowired
	private UserService userService;

	@BeforeEach
	void init() {
		// fixture
		UserDetail userDetail1 = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();
		UserDetail userDetail2 = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();
		UserDetail userDetail3 = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();

		User user1 = User.builder()
			.userId("A000001")
			.userPassword("password12!")
			.userName("방채원")
			.userMobile("010-1234-5678")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("FEMALE"))
			.userBirth("020308")
			.userEmail("glgl246@gmail.com")
			.userDepartment("DP001")
			.userPosition("PS003")
			.userTeam("TM001")
			.userRank("RK007")
			.userRole(Role.valueOf("USER"))
			.build();
		User user2 = User.builder()
			.userId("A000002")
			.userPassword("password13!")
			.userName("이윤재")
			.userMobile("010-1234-5679")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("FEMALE"))
			.userBirth("000106")
			.userEmail("avv234@naver.com")
			.userDepartment("DP001")
			.userPosition("PS002")
			.userTeam("TM001")
			.userRank("RK006")
			.userRole(Role.valueOf("USER"))
			.build();
		User user3 = User.builder()
			.userId("A000003")
			.userPassword("password14!")
			.userName("최수환")
			.userMobile("010-1234-1111")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("MALE"))
			.userBirth("980911")
			.userEmail("abc13@naver.com")
			.userDepartment("DP002")
			.userPosition("PS001")
			.userTeam("TM005")
			.userRank("RK004")
			.userRole(Role.valueOf("USER"))
			.build();
		user1.updateUserDetail(userDetail1);
		user2.updateUserDetail(userDetail2);
		user3.updateUserDetail(userDetail3);

		userService.createUser(new UserRequest(user1));
		userService.createUser(new UserRequest(user2));
		userService.createUser(new UserRequest(user3));
	}

	@Test
	@DisplayName("로그인 성공")
	void signInSuccess() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A000001")
			.userPassword("password12!")
			.build();

		// when
		TokenResponse tokenResponse = authService.signIn(signInRequest);

		// then
		assertThat(tokenResponse).isNotNull();
		assertThat(tokenResponse.getAccessToken()).isNotNull();
		assertThat(tokenResponse.getRefreshToken()).isNotNull();
	}

	@Test
	@DisplayName("로그인 실패 - 아이디 없음")
	void signInFail1() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A999999")
			.userPassword("password14!")
			.build();

		// when
		// then
		assertThatThrownBy(() -> authService.signIn(signInRequest))
			.isInstanceOf(BadCredentialsException.class)
			.hasMessage(AuthMessageConstants.AUTH_FAIL_USER_NOT_FOUND);
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void signInFail2() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A000003")
			.userPassword("password15!")
			.build();

		// when
		// then
		assertThatThrownBy(() -> authService.signIn(signInRequest))
			.isInstanceOf(BadCredentialsException.class)
			.hasMessage(AuthMessageConstants.AUTH_FAIL_PASSWORD_NOT_MATCH);
	}

	@Test
	@DisplayName("토큰 재발급 성공")
	void reIssuanceSuccess() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A000001")
			.userPassword("password12!")
			.build();
		TokenResponse tokenResponse = authService.signIn(signInRequest);

		// when
		TokenResponse reIssuance = authService.reIssuance(tokenResponse.getRefreshToken());

		// then
		assertThat(reIssuance).isNotNull();
		assertThat(reIssuance.getAccessToken()).isNotNull();
		assertThat(reIssuance.getRefreshToken()).isNotNull();
	}

	@Test
	@DisplayName("토큰 재발급 실패")
	void reIssuanceFail() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A000001")
			.userPassword("password12!")
			.build();
		TokenResponse tokenResponse = authService.signIn(signInRequest);

		// when & then
		assertThatThrownBy(() -> authService.reIssuance("invalidToken"))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("잘못된 JWT 형식입니다.");
	}

	@Test
	@DisplayName("로그아웃 성공")
	void signOutSuccess() {
		// given
		SignInRequest signInRequest = SignInRequest.builder()
			.userId("A000001")
			.userPassword("password12!")
			.build();
		TokenResponse tokenResponse = authService.signIn(signInRequest);

		// when
		authService.signOut(tokenResponse.getRefreshToken());

		// then
		// assertThat(newTokenResponse.getRefreshToken()).isNull();
		// refresh token이 삭제되었으므로 재발급이 불가능하다.
		assertThatThrownBy(() ->  authService.reIssuance(tokenResponse.getRefreshToken()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("유효한 토큰이 아닙니다. 다시 확인해주세요.");
	}
}