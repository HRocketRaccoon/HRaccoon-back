package org.finalpjt.hraccoon.domain.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.entity.ApprovalDetail;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalType;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.code.data.entity.Code;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AbilityRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AbilityResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private CodeRepository codeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	ApprovalRepository approvalRepository;
	@Autowired
	AbilityRepository abilityRepository;

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
	void createUser() {
	}

	@Test
	@DisplayName("사용자 디테일 정보를 생성할 수 있다.")
	void createUserDetail() {
		// given
		LocalDateTime userJoinDate = LocalDateTime.now();
		// when
		UserDetail userDetail = userService.createUserDetail(userJoinDate);
		// then
		assertThat(userDetail.getUserJoinDate()).isNotNull();
		assertThat(userDetail.getUserRemainVacation()).isEqualTo(24);
	}

	@Test
	@DisplayName("사용자 정보를 조회할 수 있다.")
	void getUserInfo() {
		// given
		String userId = "A000001";
		// when
		UserResponse userInfo = userService.getUserInfo(userId);
		// then
		assertThat(userInfo.getUserName()).isEqualTo("방채원");
		assertThat(userInfo.getUserMobile()).isEqualTo("010-1234-5678");
		assertThat(userInfo.getUserAddress()).isEqualTo("서울 강남구");
		assertThat(userInfo.getUserGender()).isEqualTo("FEMALE");
		assertThat(userInfo.getUserBirth()).isEqualTo("020308");
		assertThat(userInfo.getUserEmail()).isEqualTo("glgl246@gmail.com");
		assertThat(userInfo.getUserDepartment()).isEqualTo("DP001");
		assertThat(userInfo.getUserPosition()).isEqualTo("PS003");
		assertThat(userInfo.getUserTeam()).isEqualTo("TM001");
		assertThat(userInfo.getUserRank()).isEqualTo("RK007");
		assertThat(userInfo.getUserRole()).isEqualTo("USER");
		assertThat(userInfo.getUserRemainVacation()).isEqualTo(24);
		assertThat(userInfo.getUserJoinDate()).isNotNull();
	}

	@Test
	@DisplayName("사용자 정보를 조회 시, 사용자가 없는 경우 예외 발생")
	void getUserInfo_exception() {
		// given
		String userId = "A000004";
		// when
		// then
		assertThatThrownBy(() -> userService.getUserInfo(userId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 유저가 존재하지 않습니다.");
	}

	@Test
	void getUserBelongInfo() {
	}

	@Test
	void getUserName() {
	}

	@Test
	@DisplayName("사용자 정보를 수정할 수 있다.")
	void updateUserInfo() {
		// given
		UserInfoRequest params = UserInfoRequest.builder()
			.userId("A000001")
			.userMobile("010-9876-5432")
			.userAddress("서울특별시 성동구 한양대로 354-21")
			.userEmail("changeI@gmail.com")
			.build();
		// when
		UserResponse userResponse = userService.updateUserInfo(params);
		// then
		assertThat(userResponse.getUserMobile()).isEqualTo("010-9876-5432");
		assertThat(userResponse.getUserAddress()).isEqualTo("서울특별시 성동구 한양대로 354-21");
		assertThat(userResponse.getUserEmail()).isEqualTo("changeI@gmail.com");
	}
	@Test
	@DisplayName("사용자의 능력 정보를 조회할 수 있다.")
	void getUserAbilityInfo() {
		// given
		String userId = "A000001";
		Ability ability1 = Ability.builder()
			.abilityName("ABP001")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		Ability ability2 = Ability.builder()
			.abilityName("ABP002")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		Ability ability3 = Ability.builder()
			.abilityName("ABP003")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		// when
		abilityRepository.save(ability1);
		abilityRepository.save(ability2);
		abilityRepository.save(ability3);

		List<AbilityResponse> abilityResponses = userService.getUserAbilityInfo(userId);
		// then
		assertThat(abilityResponses.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("사용자의 능력 정보를 수정할 수 있다.")
	void updateUserAbility() {
		// given
		String userId = "A000001";
		Ability ability1 = Ability.builder()
			.abilityName("ABP001")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		Ability ability2 = Ability.builder()
			.abilityName("ABP002")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		Ability ability3 = Ability.builder()
			.abilityName("ABP003")
			.user(userRepository.findByUserId("A000001").get())
			.build();
		// when
		abilityRepository.save(ability1);
		abilityRepository.save(ability2);
		abilityRepository.save(ability3);

		List<AbilityRequest> params = new ArrayList<>();
		params.add(AbilityRequest.builder().abilityName("ABP001").build());
		params.add(AbilityRequest.builder().abilityName("ABP002").build());
		params.add(AbilityRequest.builder().abilityName("ABP006").build());

		List<AbilityResponse> abilityResponses = userService.updateUserAbility(userId, params);
		// then
		assertThat(abilityResponses.size()).isEqualTo(3);
		assertThat(abilityResponses.get(0).getAbilityName()).isEqualTo("ABP001");
		assertThat(abilityResponses.get(1).getAbilityName()).isEqualTo("ABP002");
		assertThat(abilityResponses.get(2).getAbilityName()).isEqualTo("ABP006");
	}

	@Test
	@DisplayName("검색을 통해 직원 리스트를 조회할 수 있다.")
	void searchUser() {
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
		// when
		codeRepository.save(code1);
		codeRepository.save(code2);

		Page<UserSearchResponse> userSearchResponses = userService.searchUser(keyword, ability, department, pageNumber, pageable);
		List<UserSearchResponse> users = userSearchResponses.getContent();

		// then
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUserId()).isEqualTo("A000001");
		assertThat(users.get(0).getUserDepartment()).isEqualTo("IT 사업부");
	}


	@Test
	@DisplayName("팀원 결재 정보를 조회할 수 있다.")
	void getTeamApprovalInfo() {
		// given
		String userTeam = "IT영업팀";
		ApprovalDetail approvalDetail1 = ApprovalDetail.builder()
			.approvalDetailContent("휴가")
			.approvalDetailStartDate(LocalDateTime.now())
			.approvalDetailEndDate(LocalDateTime.now())
			.approvalDetailResponseDate(LocalDateTime.now())
			.approvalDetailResponseContent("휴가")
			.build();
		Approval approval1 = Approval.builder()
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalStatus(ApprovalStatus.APPROVED)
			.approvalAuthority("A000001")
			.user(userRepository.findByUserId("A000002").get())
			.approvalDetail(approvalDetail1)
			.build();
		ApprovalDetail approvalDetail2 = ApprovalDetail.builder()
			.approvalDetailContent("휴가")
			.approvalDetailStartDate(LocalDateTime.now())
			.approvalDetailEndDate(LocalDateTime.now())
			.approvalDetailResponseDate(LocalDateTime.now())
			.approvalDetailResponseContent("휴가")
			.build();
		Approval approval2 = Approval.builder()
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalStatus(ApprovalStatus.APPROVED)
			.approvalAuthority("A000001")
			.user(userRepository.findByUserId("A000002").get())
			.approvalDetail(approvalDetail2)
			.build();
		ApprovalDetail approvalDetail3 = ApprovalDetail.builder()
			.approvalDetailContent("휴가")
			.approvalDetailStartDate(LocalDateTime.now())
			.approvalDetailEndDate(LocalDateTime.now())
			.approvalDetailResponseDate(LocalDateTime.now())
			.approvalDetailResponseContent("휴가")
			.build();
		Approval approval3 = Approval.builder()
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalStatus(ApprovalStatus.APPROVED)
			.approvalAuthority("A000001")
			.user(userRepository.findByUserId("A000003").get())
			.approvalDetail(approvalDetail3)
			.build();

		Code code1 = Code.builder()
			.codeNo("TM001")
			.codeName("IT영업팀")
			.build();
		// when
		codeRepository.save(code1);

		approvalRepository.save(approval1);
		approvalRepository.save(approval2);
		approvalRepository.save(approval3);

		List<ApprovalResponse> approvalResponses = userService.getTeamApprovalInfo(userTeam);
		// then
		assertThat(approvalResponses.size()).isEqualTo(2);
		assertThat(approvalResponses.get(0).getUserName()).isEqualTo("이윤재");
	}

}