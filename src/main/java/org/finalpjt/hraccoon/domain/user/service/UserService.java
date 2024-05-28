package org.finalpjt.hraccoon.domain.user.service;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.repository.UserDetailRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserDetailRepository userDetailRepository;

	@Transactional
	public void createUser(UserRequest params) {
		User entity = params.toEntity();

		try {
			UserDetail userDetail = createUserDetail(params.getUserJoinDate());
			entity.updateUserDetail(userDetail);

			userRepository.saveAndFlush(entity);
		} catch (Exception e) {
			log.error("error", e);
			/*
			* TODO: 위 상황에 대한 예외처리 논의
			*  - 이미 존재하는 아이디일 경우
			*  - 이미 존재하는 이메일일 경우
			*  - 이미 존재하는 핸드폰번호일 경우
			* */
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
		response.insertUserRemainVacation(entity.getUserDetail().getUserRemainVacation());

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
		response.insertUserRemainVacation(entity.getUserDetail().getUserRemainVacation());

		return response;
	}
}
