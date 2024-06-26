package org.finalpjt.hraccoon.domain.auth.ctrl;

import org.finalpjt.hraccoon.domain.auth.constant.AuthMessageConstants;
import org.finalpjt.hraccoon.domain.auth.data.request.SignInRequest;
import org.finalpjt.hraccoon.domain.auth.data.response.TokenResponse;
import org.finalpjt.hraccoon.domain.auth.service.AuthService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	public ApiResponse<TokenResponse> signIn(@RequestBody SignInRequest params) {
		TokenResponse response = authService.signIn(params);

		return ApiResponse.createSuccessWithMessage(response, AuthMessageConstants.AUTH_SUCCESS);
	}

	@PostMapping("/re-issuance")
	public ApiResponse<TokenResponse> reIssuance(HttpServletRequest request) {

		String refreshToken = request.getHeader("Authorization").substring(7);
		TokenResponse response = authService.reIssuance(refreshToken);

		return ApiResponse.createSuccessWithMessage(response, AuthMessageConstants.AUTH_TOKEN_REFRESH);
	}

	@PostMapping("/sign-out")
	public ApiResponse<String> signOut(HttpServletRequest request) {
		String refreshToken = request.getHeader("Authorization").substring(7);
		authService.signOut(refreshToken);
		return ApiResponse.createSuccessWithMessage(null, AuthMessageConstants.AUTH_LOGOUT);
	}
}
