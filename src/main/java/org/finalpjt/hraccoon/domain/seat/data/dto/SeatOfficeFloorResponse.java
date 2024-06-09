package org.finalpjt.hraccoon.domain.seat.data.dto;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatOfficeFloorResponse {

	private Long seatStatusNo;
	private boolean seatStatusYn;
	private String seatLocation;
	private String seatOffice;
	private String seatFloor;
	private String seatNum;

	public SeatOfficeFloorResponse(SeatStatus seatStatus) {
		this.seatStatusNo = seatStatus.getSeatStatusNo();
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
