package org.finalpjt.hraccoon.domain.user.data.dto.response;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserBelongInfoResponse {

	private String userTeam;
	private String userPosition;
	private String userName;

	public void of(User user) {
		this.userTeam = user.getUserTeam();
		this.userPosition = user.getUserPosition();
		this.userName = user.getUserName();
	}

	public void transferCode(String userTeam, String userPosition) {
		this.userTeam = userTeam;
		this.userPosition = userPosition;
	}
}
