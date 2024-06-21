package org.finalpjt.hraccoon.domain.notification.ctrl;

import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.notification.service.EmailService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification/email")
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/send/{userId}")
	public ApiResponse<?> sendNotificationEmail(@PathVariable String userId, @RequestBody ApprovalRequest params) {
		emailService.sendApprovalEmail(userId, params);

		return ApiResponse.createSuccessWithNoContent();
	}

	@PostMapping("/send/result/{userId}/{approvalNo}")
	public ApiResponse<?> sendNotificationResultEmail(@PathVariable String userId, @PathVariable Long approvalNo) {
		emailService.sendApprovalResultEmail(userId, approvalNo);

		return ApiResponse.createSuccessWithNoContent();
	}
}
