package org.finalpjt.hraccoon.domain.user.data.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_no")
	private Long userNo;

	@Column(name = "user_id", unique = true, nullable = false)
	private String userId;

	@Column(name = "user_password", nullable = false)
	private String userPassword;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "user_mobile", unique = true, nullable = false)
	private String userMobile;

	@Column(name = "user_address", nullable = false)
	private String userAddress;

	// TODO: Enum으로 변경
	@Column(name = "user_gender", nullable = false)
	private String userGender;

	// TODO: 8자리 조건 추가
	@Column(name = "user_birth", nullable = false)
	private String userBirth;

	@Column(name = "user_email", unique = true, nullable = false)
	private String userEmail;

	@Column(name = "user_department", nullable = false)
	private String userDepartment;

	@Column(name = "user_position")
	private String userPosition;

	@Column(name = "user_team", nullable = false)
	private String userTeam;

	@Column(name = "user_rank", nullable = false)
	private String userRank;

	@UpdateTimestamp
	@Column(name = "user_update_date", nullable = false)
	private LocalDateTime userUpdateDate;

	//TODO: Enum으로 변경
	@Column(name = "user_role", nullable = false)
	private String userRole;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_detail_no")
	private UserDetail userDetail;
}
