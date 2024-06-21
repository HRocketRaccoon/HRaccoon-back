package org.finalpjt.hraccoon.domain.notification.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.constant.ApprovalMessageConstants;
import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.notification.data.entity.EmailNotification;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final UserRepository userRepository;

	private final CodeRepository codeRepository;

	private final ApprovalRepository approvalRepository;

	private final MailService mailService;

	public void sendApprovalEmail(String userId, ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findByUserId(userId);
		User user = userOptional.get();

		Optional<User> approvalAuthorityUser = userRepository.findByUserId(params.getSelectedApprovalAuthority());

		EmailNotification email = EmailNotification.builder()
			.emailNotificationRecipient(params.getSelectedApprovalAuthority())
			.emailNotificationSender(user.getUserId())
			.emailNotificationAddress(approvalAuthorityUser.get().getUserEmail())
			.emailNotificationSubject("결재 요청 알림")
			.emailNotificationContent(
				user.getUserName() + " " + codeRepository.findCodeNameByCodeNo(user.getUserRank())
					+ "의 새로운 결재 요청이 등록되었습니다.")
			.emailNotificationSendAt(LocalDateTime.now())
			.build();

		mailService.saveEmailNotification(email);

		try {
			mailService.sendNotificationEmail(email);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendApprovalResultEmail(String userId, Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);
		Approval approval = approvalOptional.get();

		EmailNotification email = EmailNotification.builder()
			.emailNotificationRecipient(approval.getUser().getUserId())
			.emailNotificationSender(userId)
			.emailNotificationAddress(approval.getUser().getUserEmail())
			.emailNotificationSubject("결재 결과 알림")
			.emailNotificationContent(
				transferApprovalType(String.valueOf(approval.getApprovalType())) + "에 대한 승인/ 반려가 결정되었습니다.")
			.emailNotificationSendAt(LocalDateTime.now())
			.build();

		mailService.saveEmailNotification(email);

		try {
			mailService.sendNotificationEmail(email);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String transferApprovalType(String type) {
		return switch (type) {
			case "BUSINESS_TRIP" -> "출장";
			case "OUT_ON_BUSINESS" -> "외근";
			case "VACATION" -> "휴가";
			default -> throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_TYPE_NOT_FOUND);
		};
	}
}
