package org.finalpjt.hraccoon.domain.approval.data.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.entity.ApprovalDetail;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalType;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalRequest {

	@NotBlank
	private Long userNo;

	@NotBlank
	private ApprovalType approvalType;

	@NotBlank
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime approvalDetailStartDate;

	@NotBlank
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime approvalDetailEndDate;

	@NotBlank
	private String approvalDetailContent;

	public Approval toEntity(User user, String approvalAuthority) {
		return Approval.builder()
			.approvalType(this.approvalType)
			.user(user)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalStatus(ApprovalStatus.PENDING)
			.approvalAuthority(approvalAuthority)
			.approvalDetail(ApprovalDetail.builder()
				.approvalDetailContent(this.approvalDetailContent)
				.approvalDetailStartDate(this.approvalDetailStartDate)
				.approvalDetailEndDate(this.approvalDetailEndDate)
				.build())
			.build();
	}
}
