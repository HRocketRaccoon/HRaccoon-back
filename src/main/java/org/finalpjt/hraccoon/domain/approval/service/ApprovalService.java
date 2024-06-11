package org.finalpjt.hraccoon.domain.approval.service;

import java.time.LocalDateTime;
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
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {

	private final ApprovalRepository approvalRepository;
	private final UserRepository userRepository;

	@Transactional
	public void submitApproval(User user, String selectedApprovalAuthority, ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findById(user.getUserNo());

		if (userOptional.isPresent()) {
			if (params.getApprovalDetailStartDate() == null || params.getApprovalDetailEndDate() == null) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_DATE_MISSING);
			}

			if (params.getApprovalDetailContent().isEmpty()) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_CONTENT_MISSING);
			}

			Approval approval = params.toEntity(user, selectedApprovalAuthority);
			approvalRepository.save(approval);
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

		Approval approval = approvalOptional.get();

		if (approval.getUser().getUserNo().equals(userNo) && approval.getApprovalStatus() == ApprovalStatus.PENDING) {
			approvalRepository.delete(approval);
		}
	}

	@Transactional
	public Page<ApprovalResponse> submittedApprovalList(Long userNo, int pageNumber, Pageable pageable) {
		Page<Approval> approvals = approvalRepository.findByUser_UserNo(userNo,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

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
	public ApprovalResponse submittedApprovalListDetail(Long userNo, Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getUser().getUserNo().equals(userNo)) {
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
			throw new IllegalArgumentException("해당 결재안을 조회할 수 없습니다.");
		}
	}

	@Transactional
	public Page<ApprovalResponse> requestedApprovalList(Long userNo, int pageNumber, Pageable pageable) {
		Page<Approval> approvals = approvalRepository.findByApprovalAuthorityContaining(userNo,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

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
			throw new IllegalArgumentException("해당 결재안을 조회할 수 없습니다.");
		}
	}

	@Transactional
	public ApprovalResponse responseApproval(Long userNo, Long approvalNo, boolean isApproved, String rejectionReason) {
		Optional<User> userOptional = userRepository.findByUserNo(userNo);

		User user = userOptional.get();
		String userId = user.getUserId();

		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getApprovalAuthority().equals(userId)) {
			if (isApproved) {
				approval.setApprovalStatus(ApprovalStatus.APPROVED);
				approval.getApprovalDetail().setApprovalDetailResponseDate(LocalDateTime.now());
				approval.getApprovalDetail().setApprovalDetailResponseContent(null);
			} else {
				if (rejectionReason == null || rejectionReason.isEmpty()) {
					throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_REJECTION_REASON_NOT_FOUND);
				}
				approval.setApprovalStatus(ApprovalStatus.REJECTED);
				approval.getApprovalDetail().setApprovalDetailResponseDate(LocalDateTime.now());
				approval.getApprovalDetail().setApprovalDetailResponseContent(rejectionReason);
			}

			approvalRepository.save(approval);

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
			throw new IllegalArgumentException("해당 결재안을 처리할 수 없습니다.");
		}
	}
}
