package org.finalpjt.hraccoon.domain.user.ctrl;

import java.util.List;

import jakarta.validation.Valid;

import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AbilityRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AbilityResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.ApprovalResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserBelongInfoResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.service.UserService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Valid
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/user/info/{userId}")
	public ApiResponse<UserResponse> getUserInfo(@PathVariable String userId) {
		log.info("getUserInfo userId = {}", userId);

		return ApiResponse.createSuccess(userService.getUserInfo(userId));
	}

	@GetMapping("/user/info/name/{userId}")
	public ApiResponse<String> getUserName(@PathVariable String userId) {
		log.info("getUserName userId = {}", userId);

		return ApiResponse.createSuccess(userService.getUserName(userId));
	}

	@GetMapping("/user/belong-info/{userId}")
	public ApiResponse<UserBelongInfoResponse> getUserBelongInfo(@PathVariable String userId) {
		log.info("getUserBelongInfo userId = {}", userId);
		UserBelongInfoResponse response = userService.getUserBelongInfo(userId);

		return ApiResponse.createSuccess(response);
	}

	@PostMapping("/user/update")
	public ApiResponse<UserResponse> updateUserInfo(@RequestBody UserInfoRequest params) {
		log.info("updateUserInfo params = {}", params);

		return ApiResponse.createSuccessWithMessage(userService.updateUserInfo(params),
			UserMessageConstants.USER_UPDATE_SUCCESS);
	}

	@GetMapping("/user/ability/{userId}")
	public ApiResponse<List<AbilityResponse>> getUserAbilityInfo(@PathVariable String userId) {
		log.info("getUserInfo userId = {}", userId);
		return ApiResponse.createSuccess(userService.getUserAbilityInfo(userId));
	}

	@PostMapping("/user/ability/update/{userId}")
	public ApiResponse<List<AbilityResponse>> updateUserAbilityInfo(@PathVariable String userId,
		@RequestBody List<AbilityRequest> params) {
		log.info("updateUserAbilityInfo userId = {}", userId);
		List<AbilityResponse> response = userService.updateUserAbility(userId, params);

		return ApiResponse.createSuccessWithMessage(response, UserMessageConstants.USER_ABILITY_UPDATE_SUCCESS);
	}

	@GetMapping("/user/search")
	public ApiResponse<Page<UserSearchResponse>> searchEmployee(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		@RequestParam(value = "ability", defaultValue = "") String ability,
		@RequestParam(value = "department", defaultValue = "") String department,
		@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
		@PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
		log.info("pageNumber: {}",pageNumber);
		Page<UserSearchResponse> users = userService.searchUser(keyword, ability, department, pageNumber, pageable);

		return ApiResponse.createSuccess(users);
	}

	@GetMapping("/user/team/{userTeam}")
	public ApiResponse<List<ApprovalResponse>> getTeamApprovalInfo(@PathVariable String userTeam) {

		List<ApprovalResponse> responses = userService.getTeamApprovalInfo(userTeam);

		return ApiResponse.createSuccess(responses);
	}
}
