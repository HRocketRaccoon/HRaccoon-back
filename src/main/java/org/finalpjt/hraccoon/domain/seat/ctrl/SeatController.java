package org.finalpjt.hraccoon.domain.seat.ctrl;

import java.util.List;

import jakarta.validation.Valid;

import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.service.SeatService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Valid
@RestController
@RequiredArgsConstructor
public class SeatController {

	private final SeatService seatService;

	@GetMapping("/user/seatoffice/{seatOffice}")
	public ApiResponse<List<SeatOfficeResponse>> getOfficeSeatInfo(@PathVariable String seatOffice) {

		List<SeatOfficeResponse> responses = seatService.getOfficeSeatInfo(seatOffice);

		return ApiResponse.createSuccess(responses);
	}
}
