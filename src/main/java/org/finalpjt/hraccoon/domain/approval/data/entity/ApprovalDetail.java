package org.finalpjt.hraccoon.domain.approval.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_detail_no", nullable = false)
	private Long approvalDetailNo;

	@Column(name = "approval_detail_content", nullable = false)
	private String approvalDetailContent;

	@Column(name = "approval_detail_start_date")
	private LocalDateTime approvalDetailStartDate;

	@Column(name = "approval_detail_end_date")
	private LocalDateTime approvalDetailEndDate;

	@Column(name = "approval_detail_response_date")
	private LocalDateTime approvalDetailResponseDate;

	@Column(name = "approval_detail_response_content")
	private String approvalDetailResponseContent;

	@OneToOne(mappedBy = "approvalDetail")
	private Approval approval;

	@Builder
	public ApprovalDetail(String approvalDetailContent, LocalDateTime approvalDetailStartDate,
		LocalDateTime approvalDetailEndDate, LocalDateTime approvalDetailResponseDate,
		String approvalDetailResponseContent) {
		this.approvalDetailContent = approvalDetailContent;
		this.approvalDetailStartDate = approvalDetailStartDate;
		this.approvalDetailEndDate = approvalDetailEndDate;
		this.approvalDetailResponseDate = approvalDetailResponseDate;
		this.approvalDetailResponseContent = approvalDetailResponseContent;
	}

	public ApprovalDetail updateApprovalDetail(LocalDateTime approvalDetailResponseDate,
		String approvalDetailResponseContent) {
		return ApprovalDetail.builder()
			.approvalDetailContent(this.approvalDetailContent)
			.approvalDetailStartDate(this.approvalDetailStartDate)
			.approvalDetailEndDate(this.approvalDetailEndDate)
			.approvalDetailResponseDate(approvalDetailResponseDate)
			.approvalDetailResponseContent(approvalDetailResponseContent)
			.build();
	}

}

