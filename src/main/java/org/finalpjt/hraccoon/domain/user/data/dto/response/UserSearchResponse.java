package org.finalpjt.hraccoon.domain.user.data.dto.response;

import java.util.List;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchResponse {
	private String userId;
	private String userName;;
	private String userDepartment;
	private String userTeam;
	private String userRole;
	private boolean userDeleteYn;

	public UserSearchResponse(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userDepartment = user.getUserDepartment();
		this.userTeam = user.getUserTeam();
		this.userRole = user.getUserRole().toString();
		this.userDeleteYn = user.getUserDetail().getUserDeleteYn();
	}

	public void transferCode(String userDepartment, String userTeam) {
		this.userDepartment = userDepartment;
		this.userTeam = userTeam;
	}

}
