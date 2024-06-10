package org.finalpjt.hraccoon.domain.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AbilityRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AbilityResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserBelongInfoResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.repository.AbilityRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserDetailRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.domain.user.sepecification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final UserDetailRepository userDetailRepository;

	private final CodeRepository codeRepository;

	private final AbilityRepository abilityRepository;

	private final ApprovalRepository approvalRepository;

	@Transactional
	public void createUser(UserRequest params) {
		String encryptedPassword = passwordEncoder.encode(params.getUserPassword());
		User entity = params.toEntity(encryptedPassword);

		try {
			UserDetail userDetail = createUserDetail(params.getUserJoinDate());
			entity.updateUserDetail(userDetail);

			userRepository.saveAndFlush(entity);
		} catch (Exception e) {
			log.error("error", e);
			throw new RuntimeException(UserMessageConstants.USER_CREATE_FAIL);
		}
	}

	@Transactional
	public UserDetail createUserDetail(LocalDateTime userJoinDate) {

		UserDetail userDetail = UserDetail.builder()
			.userJoinDate(userJoinDate)
			.userRemainVacation(24)
			.build();

		return userDetailRepository.save(userDetail);
	}

	@Transactional
	public UserResponse getUserInfo(String userId) {
		User entity = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		UserResponse response = new UserResponse();
		response.of(entity);
		response.insertUserDetail(entity.getUserDetail().getUserRemainVacation(),
			entity.getUserDetail().getUserJoinDate());

		return response;
	}

	@Transactional
	public UserBelongInfoResponse getUserBelongInfo(String userId) {
		User entity = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		UserBelongInfoResponse response = new UserBelongInfoResponse();

		response.of(entity);

		String userTeam = codeRepository.findCodeNameByCodeNo(entity.getUserTeam());
		String userPosition = codeRepository.findCodeNameByCodeNo(entity.getUserPosition());

		response.transferCode(userTeam, userPosition);

		return response;
	}

	@Transactional
	public UserResponse updateUserInfo(UserInfoRequest params) {
		User entity = userRepository.findByUserId(params.getUserId())
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));
		entity.updateUserSelf(params);
		userRepository.save(entity);

		UserResponse response = new UserResponse();
		response.of(entity);
		response.insertUserDetail(entity.getUserDetail().getUserRemainVacation(),
			entity.getUserDetail().getUserJoinDate());

		return response;
	}

	@Transactional
	public List<AbilityResponse> getUserAbilityInfo(String userId) {
		List<Ability> abilities = abilityRepository.findByUserId(userId);

		return abilities.stream().map(AbilityResponse::new).toList();
	}

	@Transactional
	public List<AbilityResponse> updateUserAbility(String userId, List<AbilityRequest> params) {
		User entity = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		List<Ability> abilities = abilityRepository.findByUserId(entity.getUserId());
		abilityRepository.deleteAllInBatch(abilities);

		List<Ability> newAbilities = params.stream()
			.map(param -> param.toEntity(entity))
			.toList();

		abilityRepository.saveAll(newAbilities);

		return newAbilities.stream().map(AbilityResponse::new).toList();
	}

	@Transactional(readOnly = true)
	public Page<UserSearchResponse> searchUser(String keyword, String ability, String department, int pageNumber,
		Pageable pageable) {

		Specification<User> spec = Specification.where(UserSpecification.likeUserId(keyword))
			.or(UserSpecification.likeUserName(keyword));

		if (!department.equals("")) {

			department = codeRepository.findCodeNoByCodeName(department);

			spec = spec.and(UserSpecification.findByDepartment(department));
		}
		if (!ability.equals("")) {

			ability = codeRepository.findCodeNoByCodeName(ability);

			List<User> users = abilityRepository.findUserByAbilityName(ability);
			List<Long> userNos = users.stream().map(User::getUserNo).collect(Collectors.toList());

			spec = spec.and(UserSpecification.findByAbility(userNos));
		}

		Page<User> users = userRepository.findAll(spec,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

		return users.map(UserSearchResponse::new);
	}

	@Transactional
	public List<ApprovalResponse> getTeamApprovalInfo(String userTeam) {

		userTeam = codeRepository.findCodeNoByCodeName(userTeam);

		List<Approval> approvals = approvalRepository.findByUserTeamWithUserAndApprovalDetail(userTeam,
			ApprovalStatus.APPROVED);

		return approvals.stream().map(ApprovalResponse::new).toList();
	}

	// TODO: 비밀번호 변경
}
