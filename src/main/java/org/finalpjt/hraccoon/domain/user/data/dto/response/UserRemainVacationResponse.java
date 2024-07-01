package org.finalpjt.hraccoon.domain.user.data.dto.response;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRemainVacationResponse {

	private Integer userRemainVacation;

	public UserRemainVacationResponse(Integer userRemainVacation) {
		this.userRemainVacation = userRemainVacation;
	}
}
