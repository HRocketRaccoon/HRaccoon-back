package org.finalpjt.hraccoon.domain.user.ctrl;

import jakarta.validation.Valid;

import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.dto.request.AdminUserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserDeleteRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.dto.response.AdminUserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserResponse;
import org.finalpjt.hraccoon.domain.user.data.dto.response.UserSearchResponse;
import org.finalpjt.hraccoon.domain.user.service.AdminService;
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
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/user/admin/search")
	public ApiResponse<Page<UserSearchResponse>> adminSearchUser(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		@RequestParam(value = "ability", defaultValue = "") String ability,
		@RequestParam(value = "department", defaultValue = "") String department,
		@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
		@PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
		log.info("pageNumber: {}",pageNumber);
		Page<UserSearchResponse> users = adminService.adminSearchUser(keyword, ability, department, pageNumber, pageable);

		return ApiResponse.createSuccess(users);
	}

	@PostMapping("/user/admin/create")
	public ApiResponse<Void> createUser(@RequestBody UserRequest params) {
		log.info("createUserInfo params = {}", params);
		adminService.createUser(params);

		return ApiResponse.createSuccessWithMessage(null, UserMessageConstants.USER_CREATE_SUCCESS);
	}

	@PostMapping("/user/admin/update")
	public ApiResponse<UserResponse> updateUserInfo(@RequestBody AdminUserRequest params) {
		log.info("updateUserInfo params = {}", params);

		return ApiResponse.createSuccessWithMessage(adminService.updateUserInfo(params),
			UserMessageConstants.USER_UPDATE_SUCCESS);
	}
	@PostMapping("/user/admin/delete")
	public ApiResponse<?> deleteUser(@RequestBody UserDeleteRequest params){
		adminService.deleteUser(params);

		return ApiResponse.createSuccessWithNoContent();
	}

	@GetMapping("/user/admin/info/{userId}")
	public ApiResponse<AdminUserResponse> getUserInfo(@PathVariable String userId) {
		log.info("getUserInfo userId = {}", userId);

		return ApiResponse.createSuccess(adminService.getUserInfo(userId));
	}

}