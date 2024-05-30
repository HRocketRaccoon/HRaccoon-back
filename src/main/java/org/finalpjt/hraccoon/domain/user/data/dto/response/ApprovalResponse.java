package org.finalpjt.hraccoon.domain.user.data.dto.response;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApprovalResponse {
	private String userName;
	private String userTeam;
	private String approvalType;
	private LocalDateTime approvalDetailStartDate;
	private LocalDateTime approvalDetailEndDate;

	public  ApprovalResponse(Approval approval) {
		this.userName = approval.getUser().getUserName();
		this.userTeam = approval.getUser().getUserTeam();
		this.approvalType = approval.getApprovalType();
		this.approvalDetailStartDate = approval.getApprovalDetail().getApprovalDetailStartDate();
		this.approvalDetailEndDate = approval.getApprovalDetail().getApprovalDetailEndDate();
	}
}
