package org.finalpjt.hraccoon.domain.user.data.dto.request;

import org.finalpjt.hraccoon.domain.user.constant.ValidationConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoRequest {

	@NotBlank
	private String userId;

	@NotBlank
	@Pattern(regexp = ValidationConstants.MOBILE_PATTERN, message = ValidationConstants.MOBILE_MESSAGE)
	private String userMobile;

	@NotBlank
	private String userAddress;

	@NotBlank
	@Email(message = ValidationConstants.EMAIL_MESSAGE)
	private String userEmail;

	@Builder
	public UserInfoRequest(String userId, String userMobile, String userAddress, String userEmail) {
		this.userId = userId;
		this.userMobile = userMobile;
		this.userAddress = userAddress;
		this.userEmail = userEmail;
	}
}
