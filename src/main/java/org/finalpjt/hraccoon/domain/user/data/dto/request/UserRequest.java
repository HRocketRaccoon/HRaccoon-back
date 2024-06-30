package org.finalpjt.hraccoon.domain.user.data.dto.request;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.constant.ValidationConstants;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {

	@NotBlank
	private String userId;

	@NotBlank
	@Size(min = 8, max = 16, message = ValidationConstants.PASSWORD_SIZE_MESSAGE)
	@Pattern(regexp = ValidationConstants.PASSWORD_PATTERN, message = ValidationConstants.PASSWORD_PATTERN_MESSAGE)
	private String userPassword;

	@NotBlank
	private String userName;

	@NotBlank
	@Pattern(regexp = ValidationConstants.MOBILE_PATTERN, message = ValidationConstants.MOBILE_MESSAGE)
	private String userMobile;

	@NotBlank
	private String userAddress;

	@NotBlank
	@Pattern(regexp = ValidationConstants.GENDER_PATTERN, message = ValidationConstants.GENDER_MESSAGE)
	private String userGender;

	@NotBlank
	private String userBirth;

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

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime userJoinDate;

	@NotBlank
	private String userImageUrl;

	public User toEntity(String encryptedPassword) {
		return User.builder()
			.userId(userId)
			.userPassword(encryptedPassword)
			.userName(userName)
			.userMobile(userMobile)
			.userAddress(userAddress)
			.userGender(Gender.valueOf(userGender))
			.userBirth(userBirth)
			.userEmail(userEmail)
			.userDepartment(userDepartment)
			.userPosition(userPosition)
			.userTeam(userTeam)
			.userRank(userRank)
			.userRole(Role.valueOf(userRole))
			.build();
	}

	public UserRequest(User user) {
		this.userId = user.getUserId();
		this.userPassword = user.getUserPassword();
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
		this.userJoinDate = user.getUserDetail().getUserJoinDate();
	}

	public void transferCode(String userDepartment, String userTeam, String userRank, String userPosition) {
		this.userDepartment = userDepartment;
		this.userTeam = userTeam;
		this.userRank = userRank;
		this.userPosition = userPosition;
	}
}
