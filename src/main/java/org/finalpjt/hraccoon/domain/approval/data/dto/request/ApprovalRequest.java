package org.finalpjt.hraccoon.domain.approval.data.dto.request;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.entity.ApprovalDetail;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalType;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalRequest {
	private Long userNo;
	private ApprovalType approvalType;
	private LocalDateTime approvalDetailStartDate;
	private LocalDateTime approvalDetailEndDate;
	private String approvalDetailContent;

	private String getApprovalAuthority(String userPosition) {
		if (userPosition.equals("PS000")) {
			return "PS001";
		} else if (userPosition.equals("PS001")) {
			return "PS002";
		} else {
			return "PS002"; // TODO: 사수가 없을 경우
		}
	}

	public Approval toEntity(User user) {
		return Approval.builder()
			.approvalType(this.approvalType)
			.user(user)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalStatus(ApprovalStatus.PENDING)
			.approvalAuthority(getApprovalAuthority(user.getUserPosition())) // TODO: 유저의 팀이 아니라 사수를 가져와야함
			.approvalDetail(ApprovalDetail.builder()
				.approvalDetailContent(this.approvalDetailContent)
				.approvalDetailStartDate(this.approvalDetailStartDate)
				.approvalDetailEndDate(this.approvalDetailEndDate)
				.build())
			.build();
	}
}
