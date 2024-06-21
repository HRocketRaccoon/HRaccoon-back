package org.finalpjt.hraccoon.domain.user.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.finalpjt.hraccoon.domain.user.constant.ValidationConstants;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminUserRequest {

	private String userId;

	@NotBlank
	private String userName;

	@NotBlank
	@Pattern(regexp = ValidationConstants.MOBILE_PATTERN, message = ValidationConstants.MOBILE_MESSAGE)
	private String userMobile;

	@NotBlank
	private String userAddress;

	@NotBlank
	@Email(message = ValidationConstants.EMAIL_MESSAGE)
	private String userEmail;

	@NotBlank
	private String userDepartment;

	@NotBlank
	private String userPosition;

	@NotBlank
	private String userTeam;

	@NotBlank
	private String userRank;

	@NotBlank
	@Pattern(regexp = ValidationConstants.ROLE_PATTERN, message = ValidationConstants.ROLE_MESSAGE)
	private String userRole;

	@Builder
	public AdminUserRequest(String userId, String userName, String userMobile, String userAddress, String userEmail,
			String userDepartment, String userPosition, String userTeam, String userRank, String userRole) {
		this.userId = userId;
		this.userName = userName;
		this.userMobile = userMobile;
		this.userAddress = userAddress;
		this.userEmail = userEmail;
		this.userDepartment = userDepartment;
		this.userPosition = userPosition;
		this.userTeam = userTeam;
		this.userRank = userRank;
		this.userRole = userRole;
	}

	public void transferCode(String userDepartment, String userPosition, String userTeam, String userRank) {
		this.userDepartment = userDepartment;
		this.userPosition = userPosition;
		this.userTeam = userTeam;
		this.userRank = userRank;
	}
}
