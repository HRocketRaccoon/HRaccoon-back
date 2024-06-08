package org.finalpjt.hraccoon.domain.seat.data.dto;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatUsingUserResponse {

	private String userId;
	private String userName;
	private String userPosition;
	private String userTeam;

	public SeatUsingUserResponse(SeatStatus seatStatus) {
		this.userId = seatStatus.getUser().getUserId();
		this.userName = seatStatus.getUser().getUserName();
		this.userPosition = seatStatus.getUser().getUserPosition();
		this.userTeam = seatStatus.getUser().getUserTeam();
	}
}
