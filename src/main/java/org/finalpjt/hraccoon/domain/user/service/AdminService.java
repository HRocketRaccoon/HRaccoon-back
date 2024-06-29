package org.finalpjt.hraccoon.domain.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AdminUserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserDeleteRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AdminUserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.repository.AbilityRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserDetailRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.domain.user.sepecification.UserSpecification;
import org.hibernate.persister.entity.mutation.EntityTableMapping;
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
public class AdminService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final UserDetailRepository userDetailRepository;

	private final CodeRepository codeRepository;

	private final AbilityRepository abilityRepository;

	@Transactional(readOnly = true)
	public Page<UserSearchResponse> adminSearchUser(String keyword, String ability, String department, String deleteYn, int pageNumber,
		Pageable pageable) {

		if (!keyword.equals("") && userRepository.findByUserId(keyword).isEmpty() && userRepository.findByUserName(
			keyword).isEmpty()) {
			throw new IllegalArgumentException(UserMessageConstants.USER_SEARCH_FAIL);
		}

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
		if (!deleteYn.equals("")) {

			boolean userDeleteYn;
			if(deleteYn.equals("퇴사")) {
				userDeleteYn = true;
			} else {
				userDeleteYn = false;
			}
			spec = spec.and(UserSpecification.findByDeleteYn(userDeleteYn));
		}

		Page<User> users = userRepository.findAll(spec,
			PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort()));

		Page<UserSearchResponse> responses = users.map(UserSearchResponse::new);

		for (UserSearchResponse response : responses.getContent()) {
			response.transferCode(codeRepository.findCodeNameByCodeNo(response.getUserDepartment()),
				codeRepository.findCodeNameByCodeNo(response.getUserTeam()));
		}

		return responses;
	}

	@Transactional
	public void createUser(UserRequest params) {
		String userId = params.getUserId();

		// 이미 존재하는 사용자 ID인지 확인
		if (userRepository.findByUserId(userId).isPresent()) {
			throw new IllegalArgumentException(UserMessageConstants.USER_ALREADY_EXISTS);
		}
		// 이메일, 연락처 중복 확인
		if (userRepository.findByUserEmail(params.getUserEmail()).isPresent()) {
			throw new IllegalArgumentException(UserMessageConstants.USER_EMAIL_ALREADY_EXISTS);
		}
		if (userRepository.findByUserMobile(params.getUserMobile()).isPresent()) {
			throw new IllegalArgumentException(UserMessageConstants.USER_MOBILE_ALREADY_EXISTS);
		}

		String userDepartment = codeRepository.findCodeNoByCodeName(params.getUserDepartment());
		String userPosition = codeRepository.findCodeNoByCodeName(params.getUserPosition());
		String userTeam	= codeRepository.findCodeNoByCodeName(params.getUserTeam());
		String userRank	= codeRepository.findCodeNoByCodeName(params.getUserRank());

		params.transferCode(userDepartment, userTeam, userRank,userPosition);

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
	public UserResponse updateUserInfo(AdminUserRequest params) {
		User entity = userRepository.findByUserId(params.getUserId())
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		String userDepartment = codeRepository.findCodeNoByCodeName(params.getUserDepartment());
		String userPosition = codeRepository.findCodeNoByCodeName(params.getUserPosition());
		String userTeam = codeRepository.findCodeNoByCodeName(params.getUserTeam());
		String userRank = codeRepository.findCodeNoByCodeName(params.getUserRank());

		if (userDepartment == null || userPosition == null || userTeam == null || userRank == null) {
			throw new IllegalArgumentException(UserMessageConstants.CODE_NOT_FOUND);
		}

		params.transferCode(userDepartment, userPosition, userTeam, userRank);
		entity.updateUserAdmin(params);
		userRepository.save(entity);

		UserResponse response = new UserResponse();
		response.of(entity);
		response.insertUserDetail(entity.getUserDetail().getUserRemainVacation(),
			entity.getUserDetail().getUserJoinDate());

		return response;
	}

	@Transactional
	public void deleteUser(UserDeleteRequest params) {
		User entity = userRepository.findByUserId(params.getUserId())
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		entity.getUserDetail().updateUserDeleteYn();
		entity.getUserDetail().updateLeavingDate(LocalDateTime.now());
		entity.getUserDetail().updateLeavingReason(params.getUserDeleteReason());
		userRepository.save(entity);
	}

	@Transactional
	public AdminUserResponse getUserInfo(String userId) {
		User entity = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		AdminUserResponse response = new AdminUserResponse(entity);

		return response;
	}

	@Transactional
	public void changePassword(String userId, String newPassword, String confirmPassword) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		if (!newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException(UserMessageConstants.PASSWORD_CHANGE_FAIL_CONFIRM_ERROR);
		}

		user.updatePassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);
	}
}