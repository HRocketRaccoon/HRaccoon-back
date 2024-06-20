package org.finalpjt.hraccoon.domain.user.data.entity;

import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.global.abstracts.BaseTimeEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

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

	@Column(name = "user_gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender userGender;

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

	@Column(name = "user_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role userRole;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_detail_no")
	private UserDetail userDetail;

	// 240613 - testcode용 userNo 추가
	@Builder
	private User(String userId, String userPassword, String userName, String userMobile,
		String userAddress, Gender userGender, String userBirth, String userEmail,
		String userDepartment, String userPosition, String userTeam, String userRank,
		Role userRole, UserDetail userDetail) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userMobile = userMobile;
		this.userAddress = userAddress;
		this.userGender = userGender;
		this.userBirth = userBirth;
		this.userEmail = userEmail;
		this.userDepartment = userDepartment;
		this.userPosition = userPosition;
		this.userTeam = userTeam;
		this.userRank = userRank;
		this.userRole = userRole;
		this.userDetail = userDetail;
	}

	public void updateUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public void updateUserSelf(UserInfoRequest userInfoRequest) {
		this.userAddress = userInfoRequest.getUserAddress();
		this.userMobile = userInfoRequest.getUserMobile();
		this.userEmail = userInfoRequest.getUserEmail();
	}

	// test용 
	@Builder
	private User(Long userNo) {
		this.userNo = userNo;
	}

}
