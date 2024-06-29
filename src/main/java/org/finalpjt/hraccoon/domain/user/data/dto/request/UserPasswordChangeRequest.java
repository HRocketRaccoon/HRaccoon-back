package org.finalpjt.hraccoon.domain.user.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.finalpjt.hraccoon.domain.user.constant.ValidationConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordChangeRequest {

	@NotBlank
	private String userId;

	@NotBlank
	private String originPassword;

	@NotBlank
	@Size(min = 8, max = 16, message = ValidationConstants.PASSWORD_SIZE_MESSAGE)
	@Pattern(regexp = ValidationConstants.PASSWORD_PATTERN, message = ValidationConstants.PASSWORD_PATTERN_MESSAGE)
	private String newPassword;

	@NotBlank
	@Size(min = 8, max = 16, message = ValidationConstants.PASSWORD_SIZE_MESSAGE)
	@Pattern(regexp = ValidationConstants.PASSWORD_PATTERN, message = ValidationConstants.PASSWORD_PATTERN_MESSAGE)
	private String confirmPassword;
}
