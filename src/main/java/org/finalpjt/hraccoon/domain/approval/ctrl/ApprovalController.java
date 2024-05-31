package org.finalpjt.hraccoon.domain.approval.ctrl;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.constant.ApprovalMessageConstants;
import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.approval.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.approval.service.ApprovalService;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApprovalController {

	private final ApprovalService approvalService;
	private final UserRepository userRepository;

	@PostMapping("/approval/submit/{userNo}")
	@ResponseBody
	public ApiResponse<Void> postSubmitApproval(@PathVariable Long userNo, @RequestBody ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findById(userNo);
		User user = userOptional.get();
		approvalService.submitApproval(user, params);
		return ApiResponse.createSuccessWithMessage(null, ApprovalMessageConstants.APPROVAL_SUBMIT_SUCCESS);
	}

	@GetMapping("/approval/submittedapprovallist/{userNo}")
	public ApiResponse<List<ApprovalResponse>> getSubmittedApprovalList(@PathVariable Long userNo) {
		List<ApprovalResponse> approvalResponses = approvalService.submittedApprovalList(userNo);

		return ApiResponse.createSuccess(approvalResponses);
	}

	@GetMapping("/approval/submittedapprovallist/{userNo}/{approvalNo}")
	public ApiResponse<ApprovalResponse> getSubmittedApprovalListDetail(@PathVariable Long userNo,
		@PathVariable Long approvalNo) {
		ApprovalResponse approvalResponse = approvalService.submittedApprovalListDetail(userNo, approvalNo);

		return ApiResponse.createSuccess(approvalResponse);
	}

	@PostMapping("/approval/submittedapprovallist/{userNo}/{approvalNo}/cancel")
	public ApiResponse<Void> postCancelApproval(@PathVariable Long userNo, @PathVariable Long approvalNo) {
		approvalService.cancelApproval(userNo, approvalNo);

		return ApiResponse.createSuccessWithMessage(null, ApprovalMessageConstants.APPROVAL_CANCEL_SUCCESS);
	}
}
