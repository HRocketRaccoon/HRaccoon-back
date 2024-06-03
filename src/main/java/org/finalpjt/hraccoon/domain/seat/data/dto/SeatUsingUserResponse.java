package org.finalpjt.hraccoon.domain.seat.data.dto;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatUsingUserResponse {

	private String userId;
	private String userName;
	private String userGender;
	private String userBirth;
	private String userDepartment;
	private String userPosition;
	private String userTeam;
	private String userRank;
	private String userRole;

	public SeatUsingUserResponse(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userGender = user.getUserGender().toString();
		this.userBirth = user.getUserBirth();
		this.userDepartment = user.getUserDepartment();
		this.userPosition = user.getUserPosition();
		this.userTeam = user.getUserTeam();
		this.userRank = user.getUserRank();
		this.userRole = user.getUserRole().toString();
	}
}
