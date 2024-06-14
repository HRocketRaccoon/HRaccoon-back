package org.finalpjt.hraccoon.domain.code.service;

import java.util.List;
import java.util.stream.Collectors;

import org.finalpjt.hraccoon.domain.code.data.dto.response.CodeResponse;
import org.finalpjt.hraccoon.domain.code.data.entity.Code;
import org.finalpjt.hraccoon.domain.code.repository.CodeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeService {

	private final CodeRepository codeRepository;

	public List<CodeResponse> getAbilityCode() {
		List<Code> codeList = codeRepository.findAbilityCode();

		return codeList.stream()
			.map(code -> new CodeResponse(code.getCodeNo(), code.getCodeName()))
			.collect(Collectors.toList());
	}
}
