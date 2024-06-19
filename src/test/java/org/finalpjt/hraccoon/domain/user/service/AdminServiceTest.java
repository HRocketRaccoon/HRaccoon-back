package org.finalpjt.hraccoon.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.code.data.entity.Code;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AdminUserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserDeleteRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AdminUserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.AbilityRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AdminServiceTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AdminService adminService;
	@Autowired
	private CodeRepository codeRepository;
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

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
	}

	@Test
	@DisplayName("직원 리스트를 검색할 수 있다.")
	void adminSearchUser() {
		// given
		String keyword = "A000001";
		String ability = "";
		String department = "IT 사업부";
		int pageNumber = 1;
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("userNo")));

		Code code1 = Code.builder()
			.codeNo("DP001")
			.codeName("IT 사업부")
			.build();
		Code code2 = Code.builder()
			.codeNo("DP002")
			.codeName("IOT 사업부")
			.build();

		codeRepository.save(code1);
		codeRepository.save(code2);

		userRepository.findByUserId("A000001").get().getUserDetail().updateUserDeleteYn();
		// when
		Page<UserSearchResponse> userSearchResponses = adminService.adminSearchUser(keyword, ability, department, pageNumber, pageable);
		List<UserSearchResponse> users = userSearchResponses.getContent();

		// then
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUserId()).isEqualTo("A000001");
		assertThat(users.get(0).getUserDepartment()).isEqualTo("IT 사업부");
	}

	@Test
	@DisplayName("사용자 정보를 조회할 수 있다.")
	void getUserInfo() {
		// given
		String userId = "A000001";
		userRepository.findByUserId("A000001").get().getUserDetail().updateUserDeleteYn();
		// when
		AdminUserResponse userResponse = adminService.getUserInfo(userId);
		// then
		assertThat(userResponse.getUserId()).isEqualTo("A000001");
		assertThat(userResponse.getUserName()).isEqualTo("방채원");
		assertThat(userResponse.getUserMobile()).isEqualTo("010-1234-5678");
		assertThat(userResponse.getUserDeleteYn()).isTrue();

	}

	@Test
	@DisplayName("존재하지 않는 사용자 ID로 사용자 정보 조회 시 예외 발생")
	void getUserInfo_exception() {
		// given
		String userId = "A999999";
		// when
		// then
		assertThatThrownBy(() -> adminService.getUserInfo(userId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 유저가 존재하지 않습니다.");
	}

	@Test
	@DisplayName("사용자 정보를 생성할 수 있다.")
	void createUser() {
		// given
		UserDetail userDetail = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();
		User user = User.builder()
			.userId("A000004")
			.userPassword("password15!")
			.userName("박지훈")
			.userMobile("010-1234-5670")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("MALE"))
			.userBirth("980911")
			.userEmail("qweer1234@naver.com")
			.userDepartment("DP002")
			.userPosition("PS001")
			.userTeam("TM005")
			.userRank("RK004")
			.userRole(Role.valueOf("USER"))
			.build();
		// when
		user.updateUserDetail(userDetail);
		adminService.createUser(new UserRequest(user));
		// then
		assertThat(userRepository.findByUserId("A000004").get().getUserId()).isEqualTo("A000004");
		assertThat(userRepository.findByUserId("A000004").get().getUserName()).isEqualTo("박지훈");
		assertThat(userRepository.findByUserId("A000004").get().getUserMobile()).isEqualTo("010-1234-5670");
	}

	@Test
	@DisplayName("이미 존재하는 사용자 ID로 사용자 정보 생성하는 경우 예외 발생")
	void createUser_exception() {
		// given
		UserDetail userDetail = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();
		User user = User.builder()
			.userId("A000001")
			.userPassword("password15!")
			.userName("박지훈")
			.userMobile("010-1234-5670")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("MALE"))
			.userBirth("980911")
			.userEmail("qweer1234@naver.com")
			.userDepartment("DP002")
			.userPosition("PS001")
			.userTeam("TM005")
			.userRank("RK004")
			.userRole(Role.valueOf("USER"))
			.build();
		// when
		user.updateUserDetail(userDetail);
		// then
		assertThatThrownBy(() -> adminService.createUser(new UserRequest(user)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 존재하는 사용자 ID입니다.");
	}

	@Test
	@DisplayName("사용자 디테일 정보를 생성할 수 있다.")
	void createUserDetail() {
		// given
		LocalDateTime userJoinDate = LocalDateTime.now();
		// when
		UserDetail userDetail = adminService.createUserDetail(userJoinDate);
		// then
		assertThat(userDetail.getUserJoinDate()).isNotNull();
		assertThat(userDetail.getUserRemainVacation()).isEqualTo(24);
	}

	@Test
	@DisplayName("사용자 정보를 수정할 수 있다.")
	void updateUserInfo() {
		// given
		AdminUserRequest params = AdminUserRequest.builder()
			.userId("A000001")
			.userName("방채원")
			.userMobile("010-9876-5432")
			.userAddress("경기도 고양시 덕양구")
			.userEmail("changeI@gmail.com")
			.userDepartment("IT 사업부")
			.userPosition("부서장")
			.userTeam("IT 개발팀")
			.userRank("사원")
			.userRole("USER")
			.build();
		Code code1 = Code.builder()
			.codeNo("DP001")
			.codeName("IT 사업부")
			.build();
		Code code2 = Code.builder()
			.codeNo("PS003")
			.codeName("부서장")
			.build();
		Code code3 = Code.builder()
			.codeNo("TM001")
			.codeName("IT 개발팀")
			.build();
		Code code4 = Code.builder()
			.codeNo("RK007")
			.codeName("사원")
			.build();

		codeRepository.save(code1);
		codeRepository.save(code2);
		codeRepository.save(code3);
		codeRepository.save(code4);
		// when
		UserResponse userResponse = adminService.updateUserInfo(params);
		// then
		assertThat(userResponse.getUserMobile()).isEqualTo("010-9876-5432");
		assertThat(userResponse.getUserAddress()).isEqualTo("경기도 고양시 덕양구");
		assertThat(userResponse.getUserEmail()).isEqualTo("changeI@gmail.com");
	}

	@Test
	@DisplayName("존재하지 않는 코드로 사용자 정보 수정 시 예외 발생")
	void updateUserInfo_exception_code() {
		// given
		AdminUserRequest params = AdminUserRequest.builder()
			.userId("A000001")
			.userName("방채원")
			.userMobile("010-9876-5432")
			.userAddress("경기도 고양시 덕양구")
			.userEmail("changeI@gmail.com")
			.userDepartment("IT 사업부")
			.userPosition("아빠")
			.userTeam("IT 개발팀")
			.userRank("대리")
			.userRole("USER")
			.build();
		Code code1 = Code.builder()
			.codeNo("DP001")
			.codeName("IT 사업부")
			.build();
		Code code2 = Code.builder()
			.codeNo("PS003")
			.codeName("부서장")
			.build();
		Code code3 = Code.builder()
			.codeNo("TM001")
			.codeName("IT 개발팀")
			.build();
		Code code4 = Code.builder()
			.codeNo("RK007")
			.codeName("사원")
			.build();

		codeRepository.save(code1);
		codeRepository.save(code2);
		codeRepository.save(code3);
		codeRepository.save(code4);
		// when
		// then
		assertThatThrownBy(() -> adminService.updateUserInfo(params))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("코드가 존재하지 않습니다.");
	}

	@Test
	@DisplayName("존재하지 않는 사용자 ID로 사용자 정보 수정 시 예외 발생")
	void updateUserInfo_exception() {
		// given
		AdminUserRequest params = AdminUserRequest.builder()
			.userId("A999999")
			.userName("방채원")
			.userMobile("010-1234-5678")
			.userAddress("서울 강남구")
			.userEmail("ggg123@naver.com")
			.userDepartment("DP001")
			.userPosition("PS003")
			.userTeam("TM001")
			.userRank("RK007")
			.userRole("USER")
			.build();
		// when
		// then
		assertThatThrownBy(() -> adminService.updateUserInfo(params))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 유저가 존재하지 않습니다.");
	}
	@Test
	@DisplayName("사용자 정보를 삭제할 수 있다.")
	void deleteUser() {
		// given
		String userId = "A000001";
		String userDeleteReason = "관리자에 의한 퇴사";
		UserDeleteRequest userDeleteRequest = UserDeleteRequest.builder()
			.userId(userId)
			.userDeleteReason(userDeleteReason)
			.build();
		// when
		adminService.deleteUser(userDeleteRequest);
		// then
		assertThat(userRepository.findByUserId("A000001").get().getUserDetail().getUserDeleteYn()).isTrue();
	}
}