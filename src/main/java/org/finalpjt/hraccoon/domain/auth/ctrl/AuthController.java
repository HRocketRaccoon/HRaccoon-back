package org.finalpjt.hraccoon.domain.auth.ctrl;

import org.finalpjt.hraccoon.domain.auth.data.request.SignInRequest;
import org.finalpjt.hraccoon.domain.auth.data.response.SignInResponse;
import org.finalpjt.hraccoon.domain.auth.service.AuthService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest params) {
		SignInResponse response = authService.signIn(params);

		return ApiResponse.createSuccessWithMessage(response, "로그인에 성공했습니다.");
	}
}
