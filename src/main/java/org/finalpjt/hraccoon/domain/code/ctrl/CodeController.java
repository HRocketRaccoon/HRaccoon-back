package org.finalpjt.hraccoon.domain.code.ctrl;

import java.util.List;

import org.finalpjt.hraccoon.domain.code.data.dto.response.CodeResponse;
import org.finalpjt.hraccoon.domain.code.service.CodeService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/code")
public class CodeController {

	private final CodeService codeService;

	@GetMapping("/ability")
	public ApiResponse<List<CodeResponse>> getAbilityCode() {
		List<CodeResponse> responses = codeService.getAbilityCode();

		return ApiResponse.createSuccess(responses);
	}
}
