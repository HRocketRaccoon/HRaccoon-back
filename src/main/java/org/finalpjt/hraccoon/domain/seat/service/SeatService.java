package org.finalpjt.hraccoon.domain.seat.service;

import java.util.List;

import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.seat.repository.SeatStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatStatusRepository seatStatusRepository ;
	@Transactional
	public List<SeatOfficeResponse> getOfficeSeatInfo(String seatOffice) {

		List<SeatStatus> approvals= seatStatusRepository.findBySeatOfficeWithSeat(seatOffice);

		return approvals.stream().map(SeatOfficeResponse::new).toList();
	}
}
