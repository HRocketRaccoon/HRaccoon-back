package org.finalpjt.hraccoon.domain.user.data.dto.response;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminUserResponse {
	private String userId;
	private String userName;
	private String userGender;
	private String userBirth;
	private String userDepartment;
	private String userPosition;
	private String userTeam;
	private String userRank;
	private String userRole;
	private String userMobile;
	private String userAddress;
	private String userEmail;

	/* UserDetail */
	private Integer userRemainVacation;
	private LocalDateTime userJoinDate;

	private LocalDateTime userLeavingDate;

	private Boolean userDeleteYn;

	public AdminUserResponse(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userGender = user.getUserGender().toString();
		this.userBirth = user.getUserBirth();
		this.userDepartment = user.getUserDepartment();
		this.userPosition = user.getUserPosition();
		this.userTeam = user.getUserTeam();
		this.userRank = user.getUserRank();
		this.userRole = user.getUserRole().toString();
		this.userMobile = user.getUserMobile();
		this.userAddress = user.getUserAddress();
		this.userEmail = user.getUserEmail();

		this.userRemainVacation = user.getUserDetail().getUserRemainVacation();
		this.userJoinDate = user.getUserDetail().getUserJoinDate();
		this.userLeavingDate = user.getUserDetail().getUserLeavingDate();
		this.userDeleteYn = user.getUserDetail().getUserDeleteYn();
	}

}
