package org.finalpjt.hraccoon.domain.user.data.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteRequest {

	private String userId;

	private String userDeleteReason;

	@Builder
	public UserDeleteRequest(String userId, String userDeleteReason) {
		this.userId = userId;
		this.userDeleteReason = userDeleteReason;
	}
}
