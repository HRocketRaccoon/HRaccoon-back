package org.finalpjt.hraccoon.domain.auth.data;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PayLoad {

	/**
	 * @param userNo 유저 번호
	 * @param userId  사번
	 * @param authority  권한
	 * @param type  토큰 종류 (access, refresh)
	 */

	private final Long userNo;
	private final String userId;
	private final String authority;
	private final String type;

	@Builder
	public PayLoad(Long userNo, String userId, String authority, String type) {
		this.userNo = userNo;
		this.userId = userId;
		this.authority = authority;
		this.type = type;
	}
}
