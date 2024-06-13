package org.finalpjt.hraccoon.domain.seat.data.dto;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatUsingUserResponse {

	private String seatLocation;
	private String seatOffice;
	private String seatFloor;
	private String seatNum;

	public SeatUsingUserResponse(SeatStatus seatStatus) {
		this.seatOffice = seatStatus.getSeat().getSeatOffice();
		this.seatLocation = seatStatus.getSeat().getSeatLocation();
		parseSeatLocation(seatLocation);
	}

	private void parseSeatLocation(String seatLocation) {
		if (seatLocation != null && seatLocation.length() >= 4) {
			this.seatFloor = seatLocation.substring(2, 3);
			this.seatNum = seatLocation.substring(3);
		}
	}
}
