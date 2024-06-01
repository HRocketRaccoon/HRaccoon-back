package org.finalpjt.hraccoon.domain.seat.data.dto;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatOfficeResponse {

	private boolean seatStatusYn;
	private String seatLocation;
	private String seatOffice;
	private String seatFloor;
	private String seatNum;

	public SeatOfficeResponse(SeatStatus seatStatus) {
		this.seatStatusYn = seatStatus.isSeatStatusYn();
		this.seatLocation = seatStatus.getSeat().getSeatLocation();
		this.seatOffice = seatStatus.getSeat().getSeatOffice();
		parseSeatLocation(seatLocation);
	}

	private void parseSeatLocation(String seatLocation) {
		if (seatLocation != null && seatLocation.length() >= 4) {
			this.seatFloor = seatLocation.substring(2, 3);
			this.seatNum = seatLocation.substring(3);
		}
	}
}
