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

	// TODO: user_image 관련 처리

	/*TODO: 사원번호 제약조건 걸기*/
	@NotBlank
	private String userId;

	@NotBlank
	@Size(min = 8, max = 16, message = ValidationConstants.PASSWORD_SIZE_MESSAGE)
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

	public User toEntity() {
		return User.builder()
			.userId(userId)
			.userPassword(userPassword)
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
}
