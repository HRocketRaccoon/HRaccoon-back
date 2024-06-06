package org.finalpjt.hraccoon.domain.seat.service;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeFloorResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatUsingUserResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
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
	public List<SeatOfficeFloorResponse> getOfficeFloorSeatInfo(String seatOffice,String floor) {

		seatOffice = codeRepository.findCodeNoByCodeName(seatOffice);

		List<SeatStatus> approvals= seatStatusRepository.findBySeatOfficeAndFloorWithSeat(seatOffice,floor);

		return approvals.stream().map(SeatOfficeFloorResponse::new).toList();
	}

	@Transactional
	public SeatUsingUserResponse getSeatUsingUserInfo(Long seatStatusNo) {

		User user= seatStatusRepository.findUserBySeatStatusNoWithUser(seatStatusNo)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));
		SeatUsingUserResponse response = new SeatUsingUserResponse(user);
		return response;
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
			seatStatus.setSeatStatusYn(true);

			User user = userRepository.findById(userNo)
				.orElseThrow(() -> new IllegalArgumentException("Invalid userNo"));
			seatStatus.setUser(user);

			seatStatusRepository.save(seatStatus);
		} else {
			throw new IllegalStateException("Seat is not available");
		}
	}

	@Transactional
	public void cancelSeat(Long seatNo, Long userNo, String seatOffice) {
		log.debug("Cancelling seat: {}, user: {}, seat office: {}", seatNo, userNo, seatOffice);

		Optional<SeatStatus> seatStatusOptional = seatStatusRepository.findBySeatSeatNoAndUserUserNo(seatNo, userNo);

		if (seatStatusOptional.isPresent()) {
			SeatStatus seatStatus = seatStatusOptional.get();
			seatStatus.setSeatStatusYn(false);
			seatStatus.setUser(null);

			log.debug("Cancelling seat {} for user {}", seatNo, userNo);

			seatStatusRepository.save(seatStatus);
		} else {
			log.error("Seat {} is not currently selected by user {} in office {}", seatNo, userNo, seatOffice);
			throw new IllegalStateException("Seat is not currently selected by the user");
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
