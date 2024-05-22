package org.finalpjt.hraccoon.domain.user.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_detail_no")
	private Long userDetailNo;

	@Column(name="user_join_date", nullable = false)
	private LocalDateTime userJoinDate;

	@Column(name="user_leaving_date")
	private LocalDateTime userLeavingDate;

	@Column(name="user_leaving_reason")
	private String userLeavingReason;

	@Column(name="user_remain_vacation")
	private Integer userRemainVacation;
}
