package org.finalpjt.hraccoon.domain.approval.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.constant.ApprovalMessageConstants;
import org.finalpjt.hraccoon.domain.approval.data.dto.request.ApprovalRequest;
import org.finalpjt.hraccoon.domain.approval.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.entity.ApprovalDetail;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalType;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ApprovalServiceTest {

	@Autowired
	private ApprovalService approvalService;

	@Autowired
	private ApprovalRepository approvalRepository;

	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;
	private User user3;
	private User user4;

	@BeforeEach
	void Setup() {
		approvalRepository.deleteAll();
		userRepository.deleteAll();

		user1 = User.builder()
			.userId("A000001")
			.userPassword("A000001")
			.userName("User1")
			.userMobile("011-1234-5678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000-01-01")
			.userEmail("1111@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS000")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		user2 = User.builder()
			.userId("A000002")
			.userPassword("A000002")
			.userName("User2")
			.userMobile("012-1234-5678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000-02-02")
			.userEmail("2222@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS001")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		user3 = User.builder()
			.userId("A000003")
			.userPassword("A000003")
			.userName("User3")
			.userMobile("013-1234-5678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000-03-03")
			.userEmail("3333@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS002")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		user4 = User.builder()
			.userId("A000004")
			.userPassword("A000004")
			.userName("User4")
			.userMobile("014-1234-56678")
			.userAddress("서울시 강남구")
			.userGender(
				Gender.FEMALE)
			.userBirth("2000-04-04")
			.userEmail("4444@naver.com")
			.userDepartment("Dept1")
			.userPosition("PS003")
			.userTeam("Team1")
			.userRank("Rank1")
			.userRole(
				Role.USER)
			.build();

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);
	}

	@DisplayName("결재 상신에 성공한 경우 테스트")
	@Test
	void submitApprovalSuccess() {
		// given
		Long userNo = user1.getUserNo();
		String selectedApprovalAuthority = "A000002";
		ApprovalType approvalType = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String approvalDetailContent = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest = ApprovalRequest.builder()
			.userNo(userNo)
			.approvalType(approvalType)
			.approvalDetailStartDate(approvalDetailStartDate)
			.approvalDetailEndDate(approvalDetailEndDate)
			.selectedApprovalAuthority(selectedApprovalAuthority)
			.approvalDetailContent(approvalDetailContent)
			.build();

		// when
		approvalService.submitApproval(user1, approvalRequest);

		// then
		Page<Approval> submittedApprovals = approvalRepository.findByUser_UserNo(userNo, PageRequest.of(0, 10));
		assertThat(submittedApprovals).hasSize(1);

		Approval submittedApproval = submittedApprovals.getContent().get(0);

		assertThat(submittedApproval.getUser().getUserNo()).isEqualTo(userNo);
		assertThat(submittedApproval.getApprovalType()).isEqualTo(approvalType);
		assertThat(submittedApproval.getApprovalSubmitDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(submittedApproval.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
		assertThat(submittedApproval.getApprovalAuthority()).isEqualTo(selectedApprovalAuthority);
		assertThat(submittedApproval.getApprovalDetail().getApprovalDetailContent()).isEqualTo(approvalDetailContent);
		assertThat(submittedApproval.getApprovalDetail().getApprovalDetailStartDate()).isEqualTo(
			approvalDetailStartDate);
		assertThat(submittedApproval.getApprovalDetail().getApprovalDetailEndDate()).isEqualTo(approvalDetailEndDate);
		assertThat(submittedApproval.getApprovalDetail().getApprovalDetailResponseDate()).isNull();
		assertThat(submittedApproval.getApprovalDetail().getApprovalDetailResponseContent()).isNull();
	}

	@DisplayName("결재안의 시작일 또는 종료일을 입력하지 않아서 결재 상신에 실패한 경우 테스트")
	@Test
	void submitApprovalFailDateMissing() {
		// given
		Long userNo = user1.getUserNo();
		ApprovalType approvalType = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate = null;
		String selectedApprovalAuthority = "A000002";
		String approvalDetailContent = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest = ApprovalRequest.builder()
			.userNo(userNo)
			.approvalType(approvalType)
			.approvalDetailStartDate(approvalDetailStartDate)
			.approvalDetailEndDate(approvalDetailEndDate)
			.selectedApprovalAuthority(selectedApprovalAuthority)
			.approvalDetailContent(approvalDetailContent)
			.build();

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.submitApproval(user1, approvalRequest);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_DETAIL_DATE_MISSING;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재 내용을 입력하지 않아서 결재 상신에 실패한 경우 테스트")
	@Test
	void submitApprovalFailContentMissing() {
		Long userNo = user1.getUserNo();
		ApprovalType approvalType = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String selectedApprovalAuthority = "A000002";
		String approvalDetailContent = "";

		ApprovalRequest approvalRequest = ApprovalRequest.builder()
			.userNo(userNo)
			.approvalType(approvalType)
			.approvalDetailStartDate(approvalDetailStartDate)
			.approvalDetailEndDate(approvalDetailEndDate)
			.selectedApprovalAuthority(selectedApprovalAuthority)
			.approvalDetailContent(approvalDetailContent)
			.build();

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.submitApproval(user1, approvalRequest);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_DETAIL_CONTENT_MISSING;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재자 목록 조회에 성공한 경우 테스트")
	@Test
	void getApprovalAuthoritySuccess() {
		// given
		String userPosition1 = user1.getUserPosition();
		String userPosition2 = user2.getUserPosition();
		String userPosition3 = user3.getUserPosition();
		String userPosition4 = user4.getUserPosition();

		// when
		List<Map<String, String>> approvalAuthorities1 = approvalService.getApprovalAuthority(userPosition1);
		List<Map<String, String>> approvalAuthorities2 = approvalService.getApprovalAuthority(userPosition2);
		List<Map<String, String>> approvalAuthorities3 = approvalService.getApprovalAuthority(userPosition3);
		List<Map<String, String>> approvalAuthorities4 = approvalService.getApprovalAuthority(userPosition4);

		// then
		assertThat(approvalAuthorities1).hasSize(3)
			.extracting("userId")
			.containsExactlyInAnyOrder("A000002", "A000003", "A000004");
		assertThat(approvalAuthorities1).hasSize(3)
			.extracting("userName")
			.containsExactlyInAnyOrder("User2", "User3", "User4");

		assertThat(approvalAuthorities2).hasSize(2)
			.extracting("userId")
			.containsExactlyInAnyOrder("A000003", "A000004");
		assertThat(approvalAuthorities2).hasSize(2)
			.extracting("userName")
			.containsExactlyInAnyOrder("User3", "User4");

		assertThat(approvalAuthorities3).hasSize(1)
			.extracting("userId")
			.containsExactlyInAnyOrder("A000004");
		assertThat(approvalAuthorities3).hasSize(1)
			.extracting("userName")
			.containsExactlyInAnyOrder("User4");

		assertThat(approvalAuthorities4).hasSize(0);
	}

	@DisplayName("결재자 목록 조회에 실패한 경우 테스트")
	@Test
	void getApprovalAuthorityFail() {
		// given
		String userPosition = "PS999";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.getApprovalAuthority(userPosition);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_AUTHORITY_NOT_FOUND;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재 취소에 성공한 경우 테스트")
	@Test
	void cancelApprovalSuccess() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo = user1.getUserNo();
		Long approvalNo = approval.getApprovalNo();

		// when
		approvalService.cancelApproval(userNo, approvalNo);

		// then
		Optional<Approval> canceledApproval = approvalRepository.findById(approvalNo);

		assertThat(canceledApproval.get().getApprovalStatus()).isEqualTo(ApprovalStatus.CANCELED);
	}

	@DisplayName("상신한 결재안 목록 조회에 성공한 경우 테스트")
	@Test
	void getSubmittedApprovalListSuccess() {
		// given
		Long userNo1 = user1.getUserNo();
		ApprovalType approvalType1 = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate1 = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate1 = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String selectedApprovalAuthority1 = "A000002";
		String approvalDetailContent1 = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest1 = ApprovalRequest.builder()
			.userNo(userNo1)
			.approvalType(approvalType1)
			.approvalDetailStartDate(approvalDetailStartDate1)
			.approvalDetailEndDate(approvalDetailEndDate1)
			.selectedApprovalAuthority(selectedApprovalAuthority1)
			.approvalDetailContent(approvalDetailContent1)
			.build();

		Long userNo2 = user1.getUserNo();
		ApprovalType approvalType2 = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate2 = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate2 = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String selectedApprovalAuthority2 = "A000002";
		String approvalDetailContent2 = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest2 = ApprovalRequest.builder()
			.userNo(userNo2)
			.approvalType(approvalType2)
			.approvalDetailStartDate(approvalDetailStartDate2)
			.approvalDetailEndDate(approvalDetailEndDate2)
			.selectedApprovalAuthority(selectedApprovalAuthority2)
			.approvalDetailContent(approvalDetailContent2)
			.build();

		approvalService.submitApproval(user1, approvalRequest1);
		approvalService.submitApproval(user1, approvalRequest2);

		int pageNumber = 1;
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("approvalNo").ascending());

		// when
		Page<ApprovalResponse> submittedApprovals = approvalService.submittedApprovalList(userNo1, pageNumber,
			pageable);

		// then
		assertThat(submittedApprovals.getContent()).hasSize(2);

		assertThat(submittedApprovals.getContent().get(0).getApprovalType()).isEqualTo(approvalType1);
		assertThat(submittedApprovals.getContent().get(0).getApprovalSubmitDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(submittedApprovals.getContent().get(0).getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
		assertThat(submittedApprovals.getContent().get(0).getApprovalAuthority()).isEqualTo(
			selectedApprovalAuthority1);
		assertThat(submittedApprovals.getContent().get(0).getApprovalDetailContent()).isEqualTo(
			approvalDetailContent1);
		assertThat(submittedApprovals.getContent().get(0).getApprovalDetailStartDate()).isEqualTo(
			approvalDetailStartDate1);
		assertThat(submittedApprovals.getContent().get(0).getApprovalDetailEndDate()).isEqualTo(
			approvalDetailEndDate1);
		assertThat(submittedApprovals.getContent().get(0).getApprovalDetailResponseDate()).isNull();
		assertThat(submittedApprovals.getContent().get(0).getApprovalDetailResponseContent()).isNull();

		assertThat(submittedApprovals.getContent().get(1).getApprovalType()).isEqualTo(approvalType2);
		assertThat(submittedApprovals.getContent().get(1).getApprovalSubmitDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(submittedApprovals.getContent().get(1).getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
		assertThat(submittedApprovals.getContent().get(1).getApprovalAuthority()).isEqualTo(
			selectedApprovalAuthority2);
		assertThat(submittedApprovals.getContent().get(1).getApprovalDetailContent()).isEqualTo(
			approvalDetailContent2);
		assertThat(submittedApprovals.getContent().get(1).getApprovalDetailStartDate()).isEqualTo(
			approvalDetailStartDate2);
		assertThat(submittedApprovals.getContent().get(1).getApprovalDetailEndDate()).isEqualTo(
			approvalDetailEndDate2);
		assertThat(submittedApprovals.getContent().get(1).getApprovalDetailResponseDate()).isNull();
		assertThat(submittedApprovals.getContent().get(1).getApprovalDetailResponseContent()).isNull();
	}

	@DisplayName("상신한 결재안 단건 조회에 성공한 경우 테스트")
	@Test
	void getSubmittedApprovalListDetailSuccess() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo = user1.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		ApprovalType approvalType = approval.getApprovalType();
		LocalDateTime approvalSubmitDate = approval.getApprovalSubmitDate();
		ApprovalStatus approvalStatus = approval.getApprovalStatus();
		LocalDateTime approvalDetailStartDate = approval.getApprovalDetail().getApprovalDetailStartDate();
		LocalDateTime approvalDetailEndDate = approval.getApprovalDetail().getApprovalDetailEndDate();
		String selectedApprovalAuthority = approval.getApprovalAuthority();
		String approvalDetailContent = approval.getApprovalDetail().getApprovalDetailContent();

		// when
		ApprovalResponse approvalResponse = approvalService.submittedApprovalListDetail(userNo,
			approvalNo);

		// then
		assertThat(approvalResponse.getApprovalType()).isEqualTo(approvalType);
		assertThat(approvalResponse.getApprovalSubmitDate()).isCloseTo(approvalSubmitDate,
			within(1, ChronoUnit.SECONDS));
		assertThat(approvalResponse.getApprovalStatus()).isEqualTo(approvalStatus);
		assertThat(approvalResponse.getApprovalAuthority()).isEqualTo(selectedApprovalAuthority);
		assertThat(approvalResponse.getApprovalDetailContent()).isEqualTo(approvalDetailContent);
		assertThat(approvalResponse.getApprovalDetailStartDate()).isEqualTo(approvalDetailStartDate);
		assertThat(approvalResponse.getApprovalDetailEndDate()).isEqualTo(approvalDetailEndDate);
		assertThat(approvalResponse.getApprovalDetailResponseDate()).isNull();
		assertThat(approvalResponse.getApprovalDetailResponseContent()).isNull();
	}

	@DisplayName("본인이 상신한 결재안이 아니라서 결재안 단건 조회에 실패한 경우 테스트")
	@Test
	void getSubmittedApprovalListDetailFail() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.submittedApprovalListDetail(userNo2, approvalNo);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_OTHER_USER_SUBMITTED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);

	}

	@DisplayName("요청된 결재안 목록 조회에 성공한 경우 테스트")
	@Test
	void getRequestedApprovalListSuccess() {
		// given
		Long userNo1 = user1.getUserNo();
		ApprovalType approvalType1 = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate1 = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate1 = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String selectedApprovalAuthority1 = "A000002";
		String approvalDetailContent1 = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest1 = ApprovalRequest.builder()
			.userNo(userNo1)
			.approvalType(approvalType1)
			.approvalDetailStartDate(approvalDetailStartDate1)
			.approvalDetailEndDate(approvalDetailEndDate1)
			.selectedApprovalAuthority(selectedApprovalAuthority1)
			.approvalDetailContent(approvalDetailContent1)
			.build();

		Long userNo2 = user1.getUserNo();
		ApprovalType approvalType2 = ApprovalType.VACATION;
		LocalDateTime approvalDetailStartDate2 = LocalDateTime.of(2024, 6, 14, 9, 0, 0);
		LocalDateTime approvalDetailEndDate2 = LocalDateTime.of(2024, 6, 17, 18, 0, 0);
		String selectedApprovalAuthority2 = "A000002";
		String approvalDetailContent2 = "Team1팀 User1 휴가 신청합니다.";

		ApprovalRequest approvalRequest2 = ApprovalRequest.builder()
			.userNo(userNo2)
			.approvalType(approvalType2)
			.approvalDetailStartDate(approvalDetailStartDate2)
			.approvalDetailEndDate(approvalDetailEndDate2)
			.selectedApprovalAuthority(selectedApprovalAuthority2)
			.approvalDetailContent(approvalDetailContent2)
			.build();

		approvalService.submitApproval(user1, approvalRequest1);
		approvalService.submitApproval(user1, approvalRequest2);

		int pageNumber = 1;
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("approvalNo").ascending());

		// when
		Page<ApprovalResponse> requestedApprovals = approvalService.requestedApprovalList(user2.getUserId(), pageNumber,
			pageable);

		// then
		assertThat(requestedApprovals.getContent()).hasSize(2);

		assertThat(requestedApprovals.getContent().get(0).getApprovalType()).isEqualTo(approvalType1);
		assertThat(requestedApprovals.getContent().get(0).getApprovalSubmitDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(requestedApprovals.getContent().get(0).getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
		assertThat(requestedApprovals.getContent().get(0).getApprovalAuthority()).isEqualTo(
			selectedApprovalAuthority1);
		assertThat(requestedApprovals.getContent().get(0).getApprovalDetailContent()).isEqualTo(
			approvalDetailContent1);
		assertThat(requestedApprovals.getContent().get(0).getApprovalDetailStartDate()).isEqualTo(
			approvalDetailStartDate1);
		assertThat(requestedApprovals.getContent().get(0).getApprovalDetailEndDate()).isEqualTo(
			approvalDetailEndDate1);
		assertThat(requestedApprovals.getContent().get(0).getApprovalDetailResponseDate()).isNull();
		assertThat(requestedApprovals.getContent().get(0).getApprovalDetailResponseContent()).isNull();

		assertThat(requestedApprovals.getContent().get(1).getApprovalType()).isEqualTo(approvalType2);
		assertThat(requestedApprovals.getContent().get(1).getApprovalSubmitDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(requestedApprovals.getContent().get(1).getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
		assertThat(requestedApprovals.getContent().get(1).getApprovalAuthority()).isEqualTo(
			selectedApprovalAuthority2);
		assertThat(requestedApprovals.getContent().get(1).getApprovalDetailContent()).isEqualTo(
			approvalDetailContent2);
		assertThat(requestedApprovals.getContent().get(1).getApprovalDetailStartDate()).isEqualTo(
			approvalDetailStartDate2);
		assertThat(requestedApprovals.getContent().get(1).getApprovalDetailEndDate()).isEqualTo(
			approvalDetailEndDate2);
		assertThat(requestedApprovals.getContent().get(1).getApprovalDetailResponseDate()).isNull();
		assertThat(requestedApprovals.getContent().get(1).getApprovalDetailResponseContent()).isNull();
	}

	@DisplayName("요청된 결재안 단건 조회에 성공한 경우 테스트")
	@Test
	void getRequestedApprovalListDetailSuccess() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.APPROVED)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		ApprovalType approvalType = approval.getApprovalType();
		LocalDateTime approvalSubmitDate = approval.getApprovalSubmitDate();
		ApprovalStatus approvalStatus = approval.getApprovalStatus();
		LocalDateTime approvalDetailStartDate = approval.getApprovalDetail().getApprovalDetailStartDate();
		LocalDateTime approvalDetailEndDate = approval.getApprovalDetail().getApprovalDetailEndDate();
		String selectedApprovalAuthority = approval.getApprovalAuthority();
		String approvalDetailContent = approval.getApprovalDetail().getApprovalDetailContent();

		// when
		ApprovalResponse approvalResponse = approvalService.requestedApprovalListDetail(userNo2,
			approvalNo);

		// then
		assertThat(approvalResponse.getApprovalType()).isEqualTo(approvalType);
		assertThat(approvalResponse.getApprovalSubmitDate()).isCloseTo(approvalSubmitDate,
			within(1, ChronoUnit.SECONDS));
		assertThat(approvalResponse.getApprovalStatus()).isEqualTo(approvalStatus);
		assertThat(approvalResponse.getApprovalAuthority()).isEqualTo(selectedApprovalAuthority);
		assertThat(approvalResponse.getApprovalDetailContent()).isEqualTo(approvalDetailContent);
		assertThat(approvalResponse.getApprovalDetailStartDate()).isEqualTo(approvalDetailStartDate);
		assertThat(approvalResponse.getApprovalDetailEndDate()).isEqualTo(approvalDetailEndDate);
		assertThat(approvalResponse.getApprovalDetailResponseDate()).isNull();
		assertThat(approvalResponse.getApprovalDetailResponseContent()).isNull();
	}

	@DisplayName("본인에게 상신된 결재안이 아니라서 결재안 단건 조회에 실패한 경우 테스트")
	@Test
	void getRequestedApprovalListDetailFail() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.APPROVED)
			.build();

		approvalRepository.save(approval);

		Long userNo3 = user3.getUserNo();
		Long approvalNp = approval.getApprovalNo();

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.requestedApprovalListDetail(userNo3, approvalNp);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_OTHER_USER_REQUESTED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재 승인에 성공한 경우 테스트")
	@Test
	void approveApprovalSuccess() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		boolean isApproved = true;
		String approvalDetailResponseContent = ApprovalMessageConstants.APPROVAL_APPROVED;

		// when
		ApprovalResponse approvalResponse = approvalService.responseApproval(userNo2, approvalNo, isApproved,
			approvalDetailResponseContent);

		// then
		assertThat(approvalResponse.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVED);
		assertThat(approvalResponse.getApprovalDetailResponseDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(approvalResponse.getApprovalDetailResponseContent()).isEqualTo(
			ApprovalMessageConstants.APPROVAL_APPROVED);
	}

	@DisplayName("결재 반려에 성공한 경우 테스트")
	@Test
	void rejectApprovalSuccess() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		boolean isApproved = false;
		String approvalDetailResponseContent = "반려 사유입니다.";

		// when
		ApprovalResponse approvalResponse = approvalService.responseApproval(userNo2, approvalNo, isApproved,
			approvalDetailResponseContent);

		// then
		assertThat(approvalResponse.getApprovalStatus()).isEqualTo(ApprovalStatus.REJECTED);
		assertThat(approvalResponse.getApprovalDetailResponseDate()).isCloseTo(LocalDateTime.now(),
			within(1, ChronoUnit.SECONDS));
		assertThat(approvalResponse.getApprovalDetailResponseContent()).isEqualTo(approvalDetailResponseContent);
	}

	@DisplayName("반려 사유를 입력하지 않아서 결재 반려에 실패한 경우 테스트")
	@Test
	void rejectApprovalFail() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		boolean isApproved = false;
		String approvalDetailResponseContent = "";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.responseApproval(userNo2, approvalNo, isApproved, approvalDetailResponseContent);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_REJECTION_REASON_NOT_FOUND;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재자가 본인이 아니라서 결재 반려에 실패한 경우 테스트")
	@Test
	void rejectApprovalFailOtherAuthority() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.PENDING)
			.build();

		approvalRepository.save(approval);

		Long userNo3 = user3.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		boolean isApproved = false;
		String approvalDetailResponseContent = "반려 사유입니다.";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.responseApproval(userNo3, approvalNo, isApproved, approvalDetailResponseContent);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_RESPONSE_NOT_ALLOWED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}

	@DisplayName("결재 상태가 대기 중이 아니라서 결재 반려에 실패한 경우 테스트")
	@Test
	void rejectApprovalFailApprovalStatusNotPending() {
		// given
		ApprovalDetail approvalDetail = ApprovalDetail.builder()
			.approvalDetailStartDate(LocalDateTime.of(2024, 6, 14, 9, 0))
			.approvalDetailEndDate(LocalDateTime.of(2024, 6, 17, 18, 0))
			.approvalDetailContent("Team1팀 User1 휴가 신청합니다.")
			.build();

		Approval approval = Approval.builder()
			.user(user1)
			.approvalType(ApprovalType.VACATION)
			.approvalSubmitDate(LocalDateTime.now())
			.approvalAuthority(user2.getUserId())
			.approvalDetail(approvalDetail)
			.approvalStatus(ApprovalStatus.APPROVED)
			.build();

		approvalRepository.save(approval);

		Long userNo2 = user2.getUserNo();
		Long approvalNo = approval.getApprovalNo();
		boolean isApproved = false;
		String approvalDetailResponseContent = "반려 사유입니다.";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			approvalService.responseApproval(userNo2, approvalNo, isApproved, approvalDetailResponseContent);
		});

		// then
		String expectedErrorMessage = ApprovalMessageConstants.APPROVAL_RESPONSE_NOT_ALLOWED;
		String actualErrorMessage = exception.getMessage();

		assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
	}
}