package org.finalpjt.hraccoon.domain.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.approval.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {

	private final ApprovalRepository approvalRepository;
	private final UserRepository userRepository;

	@Transactional
	public void submitApproval(Long userNo, ApprovalRequest params) {
		Optional<User> userOptional = userRepository.findById(userNo);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			if (params.getApprovalDetailStartDate() == null || params.getApprovalDetailEndDate() == null) {
				throw new IllegalArgumentException("시작일과 종료일을 입력해 주세요.");
			}

			if (params.getApprovalDetailContent().isEmpty()) {
				throw new IllegalArgumentException("내용을 입력해 주세요.");
			}

			Approval approval = params.toEntity(user);
			approvalRepository.save(approval);
		}
	}

	@Transactional
	public void cancelApproval(Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		Approval approval = approvalOptional.get();

		if (approval.getApprovalStatus() == ApprovalStatus.PENDING) {
			approval.setApprovalStatus(ApprovalStatus.CANCELLED);
			approvalRepository.delete(approval);
		}
	}

	@Transactional
	public List<ApprovalResponse> submittedApprovalList(Long userNo) {
		List<Approval> approvals = approvalRepository.findByUser_UserNo(userNo);
		List<ApprovalResponse> approvalResponses = new ArrayList<>();

		for (Approval approval : approvals) {
			ApprovalResponse approvalResponse = ApprovalResponse.builder()
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

			approvalResponses.add(approvalResponse);
		}

		return approvalResponses;
	}

	@Transactional
	public ApprovalResponse submittedApprovalListDetail(Long approvalNo) {
		Approval approval = approvalRepository.findByApprovalNo(approvalNo);
		ApprovalResponse approvalResponse = ApprovalResponse.builder()
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

		return approvalResponse;
	}

	// @Transactional
	// public List<Approval> requestedApprovalList(Long userNo) {
	// 	Optional<User> userOptional = userRepository.findById(userNo);
	// 	if (userOptional.isPresent()) {
	// 		User user = userOptional.get();
	// 		String userPosition = user.getUserPosition();
	// 		String userTeam = user.getUserTeam();
	//
	// 		return approvalRepository.findByApprovalAuthorityAndUserTeam(userTeam, userPosition);
	// 	} else {
	// 		throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
	// 	}
	// }

	@Transactional
	public List<Approval> requestedApprovalList(User currentUser) {
		String userTeam = currentUser.getUserTeam();
		String approvalAuthority = getApprovalAuthority(currentUser.getUserPosition());

		return approvalRepository.findByApprovalAuthorityAndUserTeam(userTeam, approvalAuthority);
	}

	private String getApprovalAuthority(String userPosition) {
		if (userPosition.equals("PS000")) {
			return "PS001";
		} else if (userPosition.equals("PS001")) {
			return "PS002";
		} else {
			return "PS002"; // TODO: 사수가 없을 경우
		}
	}
}
