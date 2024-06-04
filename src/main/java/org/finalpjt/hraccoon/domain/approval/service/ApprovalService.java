package org.finalpjt.hraccoon.domain.approval.service;

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
	public void submitApproval(User user, ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findById(user.getUserNo());
		if (userOptional.isPresent()) {
			if (params.getApprovalDetailStartDate() == null || params.getApprovalDetailEndDate() == null) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_DATE_MISSING);
			}

			if (params.getApprovalDetailContent().isEmpty()) {
				throw new IllegalArgumentException(ApprovalMessageConstants.APPROVAL_DETAIL_CONTENT_MISSING);
			}

			String approvalAuthority = getApprovalAuthority(user.getUserPosition());
			Approval approval = params.toEntity(user, approvalAuthority);
			approvalRepository.save(approval);
		}
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

	public String getApprovalAuthority(String userPosition) {
		if (userPosition.equals("PS000")) {
			return "PS001";
		} else if (userPosition.equals("PS001")) {
			return "PS002";
		} else {
			return "PS002"; // TODO: 사수가 없을 경우
		}
	}
}
