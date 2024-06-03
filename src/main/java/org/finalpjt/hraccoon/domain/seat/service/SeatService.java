package org.finalpjt.hraccoon.domain.seat.service;

import java.util.List;

import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeFloorResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatUsingUserResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatStatusRepository seatStatusRepository ;

	private final CodeRepository codeRepository;
	@Transactional
	public List<SeatOfficeResponse> getOfficeSeatInfo(String seatOffice) {

		seatOffice = codeRepository.findCodeNoByCodeName(seatOffice);

		List<SeatStatus> approvals= seatStatusRepository.findBySeatOfficeWithSeat(seatOffice);

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
}
