package org.finalpjt.hraccoon.domain.approval.data.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApprovalResponseRequest {

	@NotNull
	private Boolean isApproved;

	private String rejectionReason;
}
