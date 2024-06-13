package org.finalpjt.hraccoon.domain.seat.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.seat.constant.SeatMessageConstants;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.UserUsingSeatResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.Seat;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
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
	private SeatService seatService;

	@Autowired
	private SeatStatusRepository seatStatusRepository;

	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;

	private Seat seat1;
	private Seat seat2;
	private Seat seat3;
	private Seat seat4;
	private Seat seat5;
	private Seat seat6;
	private Seat seat7;
	private Seat seat8;
	private Seat seat9;

	@BeforeEach
	void setUp() {
		seatStatusRepository.deleteAll();
		userRepository.deleteAll();

		user1 = User.builder()
			.userId("A000001")
			.userPassword("A000001")
			.userName("User1")
			.userMobile("011-1234-5678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000")
			.userEmail("1111@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS000")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		user2 = User.builder()
			.userId("A000002")
			.userPassword("A000002")
			.userName("User2")
			.userMobile("012-1234-5678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000-01-01")
			.userEmail("2222@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS001")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		userRepository.save(user1);
		userRepository.save(user2);

		seat1 = Seat.builder()
			.seatLocation("JSL001")
			.seatOffice("OJS01")
			.build();

		seat2 = Seat.builder()
			.seatLocation("JSL002")
			.seatOffice("OJS01")
			.build();

		seat3 = Seat.builder()
			.seatLocation("JSL003")
			.seatOffice("OJS01")
			.build();

		seat4 = Seat.builder()
			.seatLocation("MPL001")
			.seatOffice("OMP02")
			.build();

		seat5 = Seat.builder()
			.seatLocation("MPL002")
			.seatOffice("OMP02")
			.build();

		seat6 = Seat.builder()
			.seatLocation("MPL003")
			.seatOffice("OMP02")
			.build();

		seat7 = Seat.builder()
			.seatLocation("SBL001")
			.seatOffice("OSB03")
			.build();

		seat8 = Seat.builder()
			.seatLocation("SBL002")
			.seatOffice("OSB03")
			.build();

		seat9 = Seat.builder()
			.seatLocation("SBL003")
			.seatOffice("OSB03")
			.build();

		SeatStatus seatStatus1 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat1)
			.build();

		SeatStatus seatStatus2 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat2)
			.build();

		SeatStatus seatStatus3 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat3)
			.build();

		SeatStatus seatStatus4 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat4)
			.build();

		SeatStatus seatStatus5 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat5)
			.build();

		SeatStatus seatStatus6 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat6)
			.build();

		SeatStatus seatStatus7 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat7)
			.build();

		SeatStatus seatStatus8 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat8)
			.build();

		SeatStatus seatStatus9 = SeatStatus.builder()
			.seatStatusYn(false)
			.seat(seat9)
			.build();

		seatStatusRepository.save(seatStatus1);
		seatStatusRepository.save(seatStatus2);
		seatStatusRepository.save(seatStatus3);
		seatStatusRepository.save(seatStatus4);
		seatStatusRepository.save(seatStatus5);
		seatStatusRepository.save(seatStatus6);
		seatStatusRepository.save(seatStatus7);
		seatStatusRepository.save(seatStatus8);
		seatStatusRepository.save(seatStatus9);
	}

	@DisplayName("특정 좌석을 이용 중인 직원의 정보를 조회하는 기능 테스트")
	@Test
	void getUserUsingSeatInfo() {
		// given
		Long seatNo = seat1.getSeatNo();
		Long userNo = user1.getUserNo();
		String seatOffice = seat1.getSeatOffice();

		seatService.selectSeat(seatNo, userNo, seatOffice);

		// when
		UserUsingSeatResponse userUsingSeatResponse = seatService.getUserUsingSeatInfo("JSL001");

		// then
		assertThat(userUsingSeatResponse.getUserId()).isEqualTo(user1.getUserId());
		assertThat(userUsingSeatResponse.getUserName()).isEqualTo(user1.getUserName());
		assertThat(userUsingSeatResponse.getUserPosition()).isEqualTo(user1.getUserPosition());
		assertThat(userUsingSeatResponse.getUserTeam()).isEqualTo(user1.getUserTeam());
	}

	@DisplayName("모든 좌석을 불러오는 기능 성공한 테스트")
	@Test
	void getAllSeats() {
		// given
		String seatOffice1 = seat1.getSeatOffice();
		String seatOffice2 = seat2.getSeatOffice();
		String seatOffice3 = seat3.getSeatOffice();

		// when
		List<SeatOfficeResponse> allSeats1 = seatService.getAllSeats(seatOffice1);
		List<SeatOfficeResponse> allSeats2 = seatService.getAllSeats(seatOffice2);
		List<SeatOfficeResponse> allSeats3 = seatService.getAllSeats(seatOffice3);

		// then
		assertThat(allSeats1).hasSize(3);
		assertThat(allSeats1.get(0).getSeatOffice()).isEqualTo(seatOffice1);
		assertThat(allSeats1.get(1).getSeatOffice()).isEqualTo(seatOffice1);
		assertThat(allSeats1.get(2).getSeatOffice()).isEqualTo(seatOffice1);

		assertThat(allSeats2).hasSize(3);
		assertThat(allSeats2.get(0).getSeatOffice()).isEqualTo(seatOffice2);
		assertThat(allSeats2.get(1).getSeatOffice()).isEqualTo(seatOffice2);
		assertThat(allSeats2.get(2).getSeatOffice()).isEqualTo(seatOffice2);

		assertThat(allSeats3).hasSize(3);
		assertThat(allSeats3.get(0).getSeatOffice()).isEqualTo(seatOffice3);
		assertThat(allSeats3.get(1).getSeatOffice()).isEqualTo(seatOffice3);
		assertThat(allSeats3.get(2).getSeatOffice()).isEqualTo(seatOffice3);
	}

	@DisplayName("잠실오피스 이용 가능 좌석 목록 조회 테스트")
	@Test
	void getAvailableSeatsJS() {
		// when
		List<SeatOfficeResponse> availableSeats = seatService.getAvailableSeats("OJS01");

		// then
		assertThat(availableSeats).hasSize(3);
		assertThat(availableSeats.get(0).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(0).getSeatLocation()).isEqualTo("JSL001");
		assertThat(availableSeats.get(0).getSeatOffice()).isEqualTo("OJS01");

		assertThat(availableSeats.get(1).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(1).getSeatLocation()).isEqualTo("JSL002");
		assertThat(availableSeats.get(1).getSeatOffice()).isEqualTo("OJS01");

		assertThat(availableSeats.get(2).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(2).getSeatLocation()).isEqualTo("JSL003");
		assertThat(availableSeats.get(2).getSeatOffice()).isEqualTo("OJS01");
	}

	@DisplayName("마포오피스 이용 가능 좌석 목록 조회 테스트")
	@Test
	void getAvailableSeatsMP() {
		// when
		List<SeatOfficeResponse> availableSeats = seatService.getAvailableSeats("OMP02");

		// then
		assertThat(availableSeats).hasSize(3);
		assertThat(availableSeats.get(0).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(0).getSeatLocation()).isEqualTo("MPL001");
		assertThat(availableSeats.get(0).getSeatOffice()).isEqualTo("OMP02");

		assertThat(availableSeats.get(1).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(1).getSeatLocation()).isEqualTo("MPL002");
		assertThat(availableSeats.get(1).getSeatOffice()).isEqualTo("OMP02");

		assertThat(availableSeats.get(2).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(2).getSeatLocation()).isEqualTo("MPL003");
		assertThat(availableSeats.get(2).getSeatOffice()).isEqualTo("OMP02");
	}

	@DisplayName("성북오피스 이용 가능 좌석 목록 조회 테스트")
	@Test
	void getAvailableSeatsSB() {
		// when
		List<SeatOfficeResponse> availableSeats = seatService.getAvailableSeats("OSB03");

		// then
		assertThat(availableSeats).hasSize(3);
		assertThat(availableSeats.get(0).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(0).getSeatLocation()).isEqualTo("SBL001");
		assertThat(availableSeats.get(0).getSeatOffice()).isEqualTo("OSB03");

		assertThat(availableSeats.get(1).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(1).getSeatLocation()).isEqualTo("SBL002");
		assertThat(availableSeats.get(1).getSeatOffice()).isEqualTo("OSB03");

		assertThat(availableSeats.get(2).isSeatStatusYn()).isFalse();
		assertThat(availableSeats.get(2).getSeatLocation()).isEqualTo("SBL003");
		assertThat(availableSeats.get(2).getSeatOffice()).isEqualTo("OSB03");
	}

	@DisplayName("정상적으로 좌석 선택에 성공한 경우 테스트")
	@Test
	void selectSeatSuccess() {
		// given
		Long seatNo = seat1.getSeatNo();
		Long userNo = user1.getUserNo();
		String seatOffice = seat1.getSeatOffice();

		// when
		seatService.selectSeat(seatNo, userNo, seatOffice);

		// then
		Optional<SeatStatus> seatStatusOptional = seatStatusRepository.findById(seatNo);

		assertThat(seatStatusOptional.get().isSeatStatusYn()).isTrue();
		assertThat(seatStatusOptional.get().getUser().getUserNo()).isEqualTo(userNo);
	}

	@DisplayName("다른 직원이 사용 중인 좌석을 선택해서 좌석 선택에 실패한 경우 테스트")
	@Test
	void selectSeatFailAlreadyInUse() {
		// given
		Long seatNo1 = seat1.getSeatNo();
		Long userNo1 = user1.getUserNo();
		String seatOffice1 = seat1.getSeatOffice();

		seatService.selectSeat(seatNo1, userNo1, seatOffice1);

		Long seatNo2 = seat2.getSeatNo();
		Long userNo2 = user2.getUserNo();
		String seatOffice2 = seat2.getSeatOffice();

		seatService.selectSeat(seatNo2, userNo2, seatOffice2);

		// when
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			seatService.selectSeat(seatNo1, userNo2, seatOffice1);
		});

		// then
		String expectedErrorMessage = SeatMessageConstants.SEAT_SELECT_NOT_ALLOWED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("정상적으로 좌석 선택 취소 성공 테스트")
	@Test
	void cancelSeatSuccess() {
		// given
		Long seatNo = seat1.getSeatNo();
		Long userNo = user1.getUserNo();
		String seatOffice = seat1.getSeatOffice();

		seatService.selectSeat(seatNo, userNo, seatOffice);

		// when
		seatService.cancelSeat(seatNo, userNo, seatOffice);

		// then
		Optional<SeatStatus> seatStatus = seatStatusRepository.findById(seatNo);
		assertThat(seatStatus.get().isSeatStatusYn()).isFalse();
		assertThat(seatStatus.get().getUser()).isNull();
	}

	@DisplayName("본인이 사용 중이 아닌 좌석 취소 테스트")
	@Test
	void cancelSeatFailNotInUse() {
		// given
		Long seatNo1 = seat1.getSeatNo();
		Long userNo1 = user1.getUserNo();
		String seatOffice1 = seat1.getSeatOffice();

		seatService.selectSeat(seatNo1, userNo1, seatOffice1);

		Long seatNo2 = seat2.getSeatNo();
		Long userNo2 = user2.getUserNo();
		String seatOffice2 = seat2.getSeatOffice();

		seatService.selectSeat(seatNo2, userNo2, seatOffice2);

		// when
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			seatService.cancelSeat(seatNo1, userNo2, seatOffice1);
		});

		// then
		String expectedErrorMessage = SeatMessageConstants.SEAT_CANCEL_NOT_ALLOWED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("선택한 자리가 있을 때 좌석 중복 선택 여부 확인 테스트")
	@Test
	void checkDuplicateSeatSelectionTrue() {
		// given
		Long seatNo = seat1.getSeatNo();
		Long userNo = user1.getUserNo();
		String seatOffice = seat1.getSeatOffice();

		seatService.selectSeat(seatNo, userNo, seatOffice);

		// when
		boolean isDuplicate = seatService.checkDuplicateSeatSelection(userNo);

		// then
		assertThat(isDuplicate).isTrue();
	}

	@DisplayName("선택한 자리가 없을 때 좌석 중복 선택 여부 확인 테스트")
	@Test
	void checkDuplicateSeatSelectionFalse() {
		// given
		Long seatNo = seat1.getSeatNo();
		Long userNo = user1.getUserNo();
		String seatOffice = seat1.getSeatOffice();

		// when
		boolean isDuplicate = seatService.checkDuplicateSeatSelection(userNo);

		// then
		assertThat(isDuplicate).isFalse();
	}
}