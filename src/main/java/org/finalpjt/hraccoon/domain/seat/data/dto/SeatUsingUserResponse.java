package org.finalpjt.hraccoon.domain.seat.data.dto;

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

	public SeatUsingUserResponse(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userPosition = user.getUserPosition();
		this.userTeam = user.getUserTeam();
	}
}
