package org.finalpjt.hraccoon.domain.user.ctrl;

import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserInfoRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.service.UserService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Valid
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user/create")
	public ApiResponse<Void> createUser(@RequestBody UserRequest params) {
		log.info("createUserInfo params = {}", params);
		userService.createUser(params);

		return ApiResponse.createSuccessWithMessage(null, UserMessageConstants.USER_CREATE_SUCCESS);
	}

	@GetMapping("/user/info/{userId}")
	public ApiResponse<UserResponse> getUserInfo(@PathVariable String userId) {
		log.info("getUserInfo userId = {}", userId);

		return ApiResponse.createSuccess(userService.getUserInfo(userId));
	}

	@PostMapping("/user/update")
	public ApiResponse<UserResponse> updateUserInfo(@RequestBody UserInfoRequest params)  {
		log.info("updateUserInfo params = {}", params);

		return ApiResponse.createSuccessWithMessage(userService.updateUserInfo(params), UserMessageConstants.USER_UPDATE_SUCCESS);
	}
}
