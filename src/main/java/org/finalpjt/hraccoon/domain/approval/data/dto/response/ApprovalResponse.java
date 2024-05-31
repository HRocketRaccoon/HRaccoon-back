package org.finalpjt.hraccoon.domain.approval.data.dto.response;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalResponse {
	private Long approvalNo;
	private String userTeam;
	private String userId;
	private String userName;
	private ApprovalType approvalType;
	private LocalDateTime approvalDetailStartDate;
	private LocalDateTime approvalDetailEndDate;
	private String approvalAuthority;
	private LocalDateTime approvalSubmitDate;
	private String approvalDetailContent;
	private ApprovalStatus approvalStatus;
	private LocalDateTime approvalDetailResponseDate;
	private String approvalDetailResponseContent;

	@Builder
	public ApprovalResponse(Long approvalNo, String userTeam, String userId, String userName, ApprovalType approvalType,
			LocalDateTime approvalDetailStartDate, LocalDateTime approvalDetailEndDate, String approvalAuthority,
			LocalDateTime approvalSubmitDate, String approvalDetailContent, ApprovalStatus approvalStatus,
			LocalDateTime approvalDetailResponseDate, String approvalDetailResponseContent) {
		this.approvalNo = approvalNo;
		this.userTeam = userTeam;
		this.userId = userId;
		this.userName = userName;
		this.approvalType = approvalType;
		this.approvalDetailStartDate = approvalDetailStartDate;
		this.approvalDetailEndDate = approvalDetailEndDate;
		this.approvalAuthority = approvalAuthority;
		this.approvalSubmitDate = approvalSubmitDate;
		this.approvalDetailContent = approvalDetailContent;
		this.approvalStatus = approvalStatus;
		this.approvalDetailResponseDate = approvalDetailResponseDate;
		this.approvalDetailResponseContent = approvalDetailResponseContent;
	}
}
