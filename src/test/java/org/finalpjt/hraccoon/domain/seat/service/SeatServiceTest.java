package org.finalpjt.hraccoon.domain.seat.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.code.data.entity.Code;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeFloorResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatUsingUserResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.UserUsingSeatResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.Seat;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.AbilityRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class SeatServiceTest {

	@Autowired
	private UserService userService;
	@Autowired
	SeatService seatService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private CodeRepository codeRepository;
	@Autowired
	SeatStatusRepository seatStatusRepository;
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
			.userEmail("unique_email_1@example.com")
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
			.userEmail("unique_email_2@example.com")
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
			.userEmail("unique_email_3@example.com")
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

		Seat seat1 = Seat.builder()
			.seatOffice("OJS01")
			.seatLocation("JSL001")
			.build();
		Seat seat2 = Seat.builder()
			.seatOffice("OJS01")
			.seatLocation("JSL002")
			.build();
		Seat seat3 = Seat.builder()
			.seatOffice("OJS01")
			.seatLocation("JS1003")
			.build();
		SeatStatus seatStatus1 = SeatStatus.builder()
			.seat(seat1)
			.seatStatusYn(false)
			.build();
		SeatStatus seatStatus2 = SeatStatus.builder()
			.seat(seat2)
			.seatStatusYn(false)
			.build();
		SeatStatus seatStatus3 = SeatStatus.builder()
			.seat(seat3)
			.seatStatusYn(true)
			.user(userRepository.findByUserId("A000001").get())
			.build();
		seatStatusRepository.save(seatStatus1);
		seatStatusRepository.save(seatStatus2);
		seatStatusRepository.save(seatStatus3);
	}

	@Test
	@DisplayName("회사별 seatStatusYn이 false인 좌석 조회")
	void getOfficeSeatInfo() {
		// given
		String seatOffice = "잠실";

		Code code1 = Code.builder()
			.codeNo("OJS01")
			.codeName("잠실")
			.build();

		codeRepository.save(code1);
		// when
		List<SeatOfficeResponse> seatOfficeResponses = seatService.getOfficeSeatInfo(seatOffice);
		// then
		assertEquals(seatOfficeResponses.size(), 2);
	}

	@Test
	@DisplayName("회사, 층에 따른 좌석 조회")
	void getOfficeFloorSeatInfo() {
		// given
		String seatOffice = "OJS01";
		String floor = "1";

		List<SeatOfficeFloorResponse> seatOfficeFloorResponses = seatService.getOfficeFloorSeatInfo(seatOffice, floor);
		// then
		assertEquals(seatOfficeFloorResponses.size(), 1);
	}
	@Test
	@DisplayName("좌석 위치로 직원 조회")
	void getUserUsingSeatInfo() {
		// given
		String seatLocation = "JS1003";
		// when
		SeatStatus seatStatus = seatStatusRepository.findUserBySeatLocationNoWithUser(seatLocation)
			.orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
		UserUsingSeatResponse response = new UserUsingSeatResponse(seatStatus);
		// then
		assertEquals(response.getUserId(), "A000001");
		assertEquals(response.getUserName(), "방채원");
	}

	@Test
	@DisplayName("사용자 아이디로 좌석 조회")
	void getSeatUsingUserInfo() {
		// given
		String userId = "A000001";
		// when
		SeatStatus seatStatus = seatStatusRepository.findSeatByUserIdWithUserAndSeat(userId)
			.orElseThrow(() -> new IllegalArgumentException("SEAT_NOT_FOUND"));
		SeatUsingUserResponse response = new SeatUsingUserResponse(seatStatus);
		// then
		assertEquals(response.getSeatLocation(), "JS1003");
	}

	@Test
	@DisplayName("사용자 아이디로 좌석 조회 시, 좌석이 없는 경우")
	void getSeatUsingUserInfo_seatNotFound() {
		// given
		String userId = "A000002";
		// when
		// then
		assertThrows(IllegalArgumentException.class, () -> seatService.getSeatUsingUserInfo(userId));
	}
}