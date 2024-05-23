package org.finalpjt.hraccoon.domain.approval.data.entity;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Approval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_no")
	private Long approvalNo;

	@Column(name = "approval_type", nullable = false)
	private String approvalType;

	@Column(name = "approval_submit_date", nullable = false)
	private LocalDateTime approvalSubmitDate;

	//TODO: Enum으로 변경
	@Column(name = "approval_status", nullable = false)
	private String approvalStatus;

	@Column(name = "approval_authority", nullable = false)
	private String approvalAuthority;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_no", nullable = false)
	private User user;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "approval_detail_no")
	private ApprovalDetail approvalDetail;
}
