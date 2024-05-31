package org.finalpjt.hraccoon.domain.seat.data.dto;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatOfficeResponse {

	private Long seatStatusNo;
	private boolean seatStatusYn;
	private String seatLocation;
	private String seatOffice;

	public  SeatOfficeResponse(SeatStatus seatStatus) {
		this.seatStatusNo=seatStatus.getSeatStatusNo();
		this.seatStatusYn=seatStatus.isSeatStatusYn();
		this.seatLocation=seatStatus.getSeat().getSeatLocation();
		this.seatOffice=seatStatus.getSeat().getSeatOffice();
	}
}
