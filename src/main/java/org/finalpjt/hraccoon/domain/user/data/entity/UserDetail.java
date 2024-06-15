package org.finalpjt.hraccoon.domain.user.data.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

	@Column(name="user_remain_vacation", columnDefinition = "integer default 15")
	private Integer userRemainVacation;

	@Builder
	public UserDetail(LocalDateTime userJoinDate, LocalDateTime userLeavingDate, String userLeavingReason, Integer userRemainVacation) {
		this.userJoinDate = userJoinDate;
		this.userLeavingDate = userLeavingDate;
		this.userLeavingReason = userLeavingReason;
		this.userRemainVacation = userRemainVacation;
	}

	public void updateJoinDate(LocalDateTime userJoinDate) {
		this.userJoinDate = userJoinDate;
	}

	public void updateRemainVacation(Integer userRemainVacation) {
		this.userRemainVacation = userRemainVacation;
	}

	public void updateLeavingDate(LocalDateTime userLeavingDate) {
		this.userLeavingDate = userLeavingDate;
	}

	public void updateLeavingReason(String userLeavingReason) {
		this.userLeavingReason = userLeavingReason;
	}
}
