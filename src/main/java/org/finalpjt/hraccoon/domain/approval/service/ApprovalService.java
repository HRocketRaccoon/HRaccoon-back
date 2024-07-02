package org.finalpjt.hraccoon.domain.approval.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.constant.ApprovalMessageConstants;
import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.approval.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.attendance.service.AttendanceService;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.notification.service.MailService;
import org.finalpjt.hraccoon.domain.notification.service.NotificationService;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {

	private final ApprovalRepository approvalRepository;

	private final UserRepository userRepository;

	private final CodeRepository codeRepository;

	private final AttendanceService attendanceService;

	private final NotificationService notificationService;

	private final MailService mailService;

	@Transactional
	public void submitApproval(User user, ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findById(user.getUserNo());

		if (userOptional.isPresent()) {
			if (params.getApprovalDetailStartDate() == null || params.getApprovalDetailEndDate() == null) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_DATE_MISSING);
			}

			if (params.getApprovalDetailContent().isEmpty()) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_CONTENT_MISSING);
			}

			Approval approval = params.toEntity(user, params.getSelectedApprovalAuthority());

			approvalRepository.save(approval);

			/* 결재 상신 알림 서비스 */
			notificationService.send(params.getSelectedApprovalAuthority(), user.getUserId(),
				user.getUserName() + " " + codeRepository.findCodeNameByCodeNo(user.getUserRank())
					+ "의 새로운 결재 요청이 등록되었습니다.");
		}
	}

	public List<Map<String, String>> getApprovalAuthority(String userPosition) {
		List<String> positions;

		switch (userPosition) {
			case "PS000":
				positions = Arrays.asList("PS001", "PS002", "PS003");
				break;
			case "PS001":
				positions = Arrays.asList("PS002", "PS003");
				break;
			case "PS002":
				positions = Arrays.asList("PS003");
				break;
			case "PS003":
				return new ArrayList<>();
			default:
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_AUTHORITY_NOT_FOUND);
		}

		List<Map<String, String>> approvalAuthorities = new ArrayList<>();

		for (String position : positions) {
			List<User> users = userRepository.findByUserPosition(position);

			for (User user : users) {
				Map<String, String> userInfo = new HashMap<>();
				userInfo.put("userId", user.getUserId());
				userInfo.put("userName", user.getUserName());
				approvalAuthorities.add(userInfo);
			}
		}

		return approvalAuthorities;
	}

	@Transactional
	public void cancelApproval(Long userNo, Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		if (approvalOptional.isPresent()) {
			Approval approval = approvalOptional.get();

			if (approval.getUser().getUserNo().equals(userNo)
				&& approval.getApprovalStatus() == ApprovalStatus.PENDING) {
				approval.cancelApproval();

				approvalRepository.save(approval);
			}
		}
	}

	@Transactional
	public Page<ApprovalResponse> submittedApprovalList(Long userNo, int pageNumber, Pageable pageable) {
		Sort sort = Sort.by(
			Sort.Order.asc("approvalStatus"),
			Sort.Order.desc("approvalSubmitDate")
		);

		Page<Approval> approvals = approvalRepository.findByUser_UserNo(userNo,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), sort));

		return approvals.map(approval -> {
			User approvalAuthorityUser = userRepository.findByUserId(approval.getApprovalAuthority()).get();
			String approvalAuthorityName = approvalAuthorityUser.getUserName();

			return ApprovalResponse.builder()
				.approvalNo(approval.getApprovalNo())
				.userTeam(approval.getUser().getUserTeam())
				.userId(approval.getUser().getUserId())
				.userName(approval.getUser().getUserName())
				.approvalType(approval.getApprovalType())
				.approvalDetailStartDate(approval.getApprovalDetail().getApprovalDetailStartDate())
				.approvalDetailEndDate(approval.getApprovalDetail().getApprovalDetailEndDate())
				.approvalAuthority(approval.getApprovalAuthority())
				.approvalAuthorityName(approvalAuthorityName)
				.approvalSubmitDate(approval.getApprovalSubmitDate())
				.approvalDetailContent(approval.getApprovalDetail().getApprovalDetailContent())
				.approvalStatus(approval.getApprovalStatus())
				.approvalDetailResponseDate(approval.getApprovalDetail().getApprovalDetailResponseDate())
				.approvalDetailResponseContent(approval.getApprovalDetail().getApprovalDetailResponseContent())
				.build();
		});
	}

	@Transactional
	public ApprovalResponse submittedApprovalListDetail(Long userNo, Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getUser().getUserNo().equals(userNo)) {
			String teamCode = approval.getUser().getUserTeam();
			String teamName = codeRepository.findCodeNameByCodeNo(teamCode);

			User approvalAuthorityUser = userRepository.findByUserId(approval.getApprovalAuthority()).get();
			String approvalAuthorityName = approvalAuthorityUser.getUserName();

			return ApprovalResponse.builder()
				.approvalNo(approval.getApprovalNo())
				.userTeam(teamName)
				.userId(approval.getUser().getUserId())
				.userName(approval.getUser().getUserName())
				.approvalType(approval.getApprovalType())
				.approvalDetailStartDate(approval.getApprovalDetail().getApprovalDetailStartDate())
				.approvalDetailEndDate(approval.getApprovalDetail().getApprovalDetailEndDate())
				.approvalAuthority(approval.getApprovalAuthority())
				.approvalAuthorityName(approvalAuthorityName)
				.approvalSubmitDate(approval.getApprovalSubmitDate())
				.approvalDetailContent(approval.getApprovalDetail().getApprovalDetailContent())
				.approvalStatus(approval.getApprovalStatus())
				.approvalDetailResponseDate(approval.getApprovalDetail().getApprovalDetailResponseDate())
				.approvalDetailResponseContent(approval.getApprovalDetail().getApprovalDetailResponseContent())
				.build();
		} else {
			throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_OTHER_USER_SUBMITTED);
		}
	}

	@Transactional
	public Page<ApprovalResponse> requestedApprovalList(String userId, int pageNumber, Pageable pageable) {
		Sort sort = Sort.by(
			Sort.Order.asc("approvalStatus"),
			Sort.Order.desc("approvalSubmitDate")
		);

		Page<Approval> approvals = approvalRepository.findByApprovalAuthority(userId,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), sort));

		return approvals.map(approval -> ApprovalResponse.builder()
			.approvalNo(approval.getApprovalNo())
			.userTeam(approval.getUser().getUserTeam())
			.userId(approval.getUser().getUserId())
			.userName(approval.getUser().getUserName())
			.approvalType(approval.getApprovalType())
			.approvalDetailStartDate(approval.getApprovalDetail().getApprovalDetailStartDate())
			.approvalDetailEndDate(approval.getApprovalDetail().getApprovalDetailEndDate())
			.approvalAuthority(approval.getApprovalAuthority())
			.approvalSubmitDate(approval.getApprovalSubmitDate())
			.approvalDetailContent(approval.getApprovalDetail().getApprovalDetailContent())
			.approvalStatus(approval.getApprovalStatus())
			.approvalDetailResponseDate(approval.getApprovalDetail().getApprovalDetailResponseDate())
			.approvalDetailResponseContent(approval.getApprovalDetail().getApprovalDetailResponseContent())
			.build());
	}

	@Transactional
	public ApprovalResponse requestedApprovalListDetail(Long userNo, Long approvalNo) {
		Optional<User> userOptional = userRepository.findByUserNo(userNo);

		User user = userOptional.get();
		String userId = user.getUserId();

		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getApprovalAuthority().equals(userId)) {
			String teamCode = approval.getUser().getUserTeam();
			String teamName = codeRepository.findCodeNameByCodeNo(teamCode);

			return ApprovalResponse.builder()
				.approvalNo(approval.getApprovalNo())
				.userTeam(teamName)
				.userId(approval.getUser().getUserId())
				.userName(approval.getUser().getUserName())
				.approvalType(approval.getApprovalType())
				.approvalDetailStartDate(approval.getApprovalDetail().getApprovalDetailStartDate())
				.approvalDetailEndDate(approval.getApprovalDetail().getApprovalDetailEndDate())
				.approvalAuthority(approval.getApprovalAuthority())
				.approvalSubmitDate(approval.getApprovalSubmitDate())
				.approvalDetailContent(approval.getApprovalDetail().getApprovalDetailContent())
				.approvalStatus(approval.getApprovalStatus())
				.approvalDetailResponseDate(approval.getApprovalDetail().getApprovalDetailResponseDate())
				.approvalDetailResponseContent(approval.getApprovalDetail().getApprovalDetailResponseContent())
				.build();
		} else {
			throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_OTHER_USER_REQUESTED);
		}
	}

	@Transactional
	public ApprovalResponse responseApproval(Long userNo, Long approvalNo, boolean isApproved,
		String approvalDetailResponseContent) {
		Optional<User> userOptional = userRepository.findByUserNo(userNo);

		User user = userOptional.get();
		String userId = user.getUserId();

		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getApprovalAuthority().equals(userId) && approval.getApprovalStatus() == ApprovalStatus.PENDING) {
			if (isApproved) {
				approval.approveApproval();
				attendanceService.updateAttendance(approvalNo);
			} else {
				if (approvalDetailResponseContent == null || approvalDetailResponseContent.isEmpty()) {
					throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_REJECTION_REASON_NOT_FOUND);
				}

				approval.rejectApproval(approvalDetailResponseContent);
			}

			approvalRepository.save(approval);

			/* 결재 결과 알림 서비스 */
			notificationService.send(approval.getUser().getUserId(), userId,
				approval.getApprovalStatus() == ApprovalStatus.APPROVED
					? transferApprovalType(String.valueOf(approval.getApprovalType())) + "에 관한 결재가 승인되었습니다." :
					transferApprovalType(String.valueOf(approval.getApprovalType())) + "에 관한 결재가 반려되었습니다.");

			return ApprovalResponse.builder()
				.approvalNo(approval.getApprovalNo())
				.userTeam(approval.getUser().getUserTeam())
				.userId(approval.getUser().getUserId())
				.userName(approval.getUser().getUserName())
				.approvalType(approval.getApprovalType())
				.approvalDetailStartDate(approval.getApprovalDetail().getApprovalDetailStartDate())
				.approvalDetailEndDate(approval.getApprovalDetail().getApprovalDetailEndDate())
				.approvalAuthority(approval.getApprovalAuthority())
				.approvalSubmitDate(approval.getApprovalSubmitDate())
				.approvalDetailContent(approval.getApprovalDetail().getApprovalDetailContent())
				.approvalStatus(approval.getApprovalStatus())
				.approvalDetailResponseDate(approval.getApprovalDetail().getApprovalDetailResponseDate())
				.approvalDetailResponseContent(approval.getApprovalDetail().getApprovalDetailResponseContent())
				.build();
		} else {
			throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_RESPONSE_NOT_ALLOWED);
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