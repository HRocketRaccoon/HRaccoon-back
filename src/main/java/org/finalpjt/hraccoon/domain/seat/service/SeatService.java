package org.finalpjt.hraccoon.domain.seat.service;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.seat.constant.SeatMessageConstants;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeFloorResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatUsingUserResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.UserUsingSeatResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatStatusRepository seatStatusRepository;
	private final CodeRepository codeRepository;
	private final UserRepository userRepository;

	@Transactional
	public List<SeatOfficeResponse> getOfficeSeatInfo(String seatOffice) {

		seatOffice = codeRepository.findCodeNoByCodeName(seatOffice);

		List<SeatStatus> approvals = seatStatusRepository.findBySeatOfficeWithSeat(seatOffice);

		return approvals.stream().map(SeatOfficeResponse::new).toList();
	}

	@Transactional
	public List<SeatOfficeFloorResponse> getOfficeFloorSeatInfo(String seatOffice, String floor) {

		List<SeatStatus> seatStatuses = seatStatusRepository.findBySeatOfficeAndFloorWithSeat(seatOffice, floor);

		List<SeatOfficeFloorResponse> seatOfficeFloorResponses = seatStatuses.stream().map(SeatOfficeFloorResponse::new).toList();

		for (SeatOfficeFloorResponse response : seatOfficeFloorResponses) {
			Optional<SeatStatus> userSeatStatus = seatStatusRepository.findUserBySeatStatusNoWithUser(response.getSeatStatusNo());
			userSeatStatus.ifPresentOrElse(
				uss -> response.updateUserId(uss.getUser().getUserId()),
				() -> response.updateUserId(null)
			);
		}

		return seatOfficeFloorResponses;
	}

	@Transactional
	public UserUsingSeatResponse getUserUsingSeatInfo(String seatLocation) {

		SeatStatus seatStatus = seatStatusRepository.findUserBySeatLocationNoWithUser(seatLocation)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));
		UserUsingSeatResponse response = new UserUsingSeatResponse(seatStatus);
		return response;
	}

	@Transactional
	public SeatUsingUserResponse getSeatUsingUserInfo(String userId) {

		SeatStatus seatStatus = seatStatusRepository.findSeatByUserIdWithUserAndSeat(userId)
			.orElseThrow(() -> new IllegalArgumentException(SeatMessageConstants.SEAT_NOT_FOUND));
		SeatUsingUserResponse response = new SeatUsingUserResponse(seatStatus);
		return response;
	}

	@Transactional(readOnly = true)
	public List<SeatOfficeResponse> getAllSeats(String seatOffice) {
		List<SeatStatus> allSeats = seatStatusRepository.findAllSeatsBySeatOffice(seatOffice);

		return allSeats.stream()
			.map(SeatOfficeResponse::new)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<SeatOfficeResponse> getAvailableSeats(String seatOffice) {
		List<SeatStatus> availableSeats = seatStatusRepository.findBySeatOfficeWithSeat(seatOffice);

		return availableSeats.stream()
			.filter(seatStatus -> !seatStatus.isSeatStatusYn())
			.map(SeatOfficeResponse::new)
			.toList();
	}

	@Transactional
	public void selectSeat(Long seatNo, Long userNo, String seatOffice) {
		List<SeatStatus> availabeSeats = seatStatusRepository.findBySeatOfficeWithSeat(seatOffice);

		Optional<SeatStatus> seatStatusOptional = availabeSeats.stream()
			.filter(seatStatus -> seatStatus.getSeat().getSeatNo().equals(seatNo) && !seatStatus.isSeatStatusYn())
			.findFirst();

		if (seatStatusOptional.isPresent()) {
			SeatStatus seatStatus = seatStatusOptional.get();

			User user = userRepository.findById(userNo).get();

			SeatStatus selectedSeatStatus = seatStatus.selectSeat(user);

			seatStatusRepository.save(selectedSeatStatus);
		} else {
			throw new IllegalStateException(SeatMessageConstants.SEAT_SELECT_NOT_ALLOWED);
		}
	}

	@Transactional
	public void cancelSeat(Long seatNo, Long userNo, String seatOffice) {
		log.debug("Cancelling seat: {}, user: {}, seat office: {}", seatNo, userNo, seatOffice);

		Optional<SeatStatus> seatStatusOptional = seatStatusRepository.findBySeatSeatNoAndUserUserNo(seatNo, userNo);

		if (seatStatusOptional.isPresent()) {
			SeatStatus seatStatus = seatStatusOptional.get();
			SeatStatus cancelledSeatStatus = seatStatus.cancelSeat();

			seatStatusRepository.save(cancelledSeatStatus);
		} else {
			throw new IllegalStateException(SeatMessageConstants.SEAT_CANCEL_NOT_ALLOWED);
		}
	}

	@Transactional(readOnly = true)
	public boolean checkDuplicateSeatSelection(Long userNo) {
		Optional<SeatStatus> seatStatusOptional = seatStatusRepository.findByUserUserNo(userNo);

		return seatStatusOptional.isPresent();
	}

	@Transactional
	public void resetSeatStatus() {
		seatStatusRepository.resetSeatStatus();
	}
}
