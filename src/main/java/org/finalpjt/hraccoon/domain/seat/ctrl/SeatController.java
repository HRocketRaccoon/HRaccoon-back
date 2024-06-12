package org.finalpjt.hraccoon.domain.seat.ctrl;

import java.util.List;

import jakarta.validation.Valid;

import org.finalpjt.hraccoon.domain.seat.constant.SeatMessageConstants;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeFloorResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatOfficeResponse;
import org.finalpjt.hraccoon.domain.seat.data.dto.SeatUsingUserResponse;
import org.finalpjt.hraccoon.domain.seat.service.SeatService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SeatController {

	private final SeatService seatService;

	@GetMapping("/seat/office/{seatOffice}")
	public ApiResponse<List<SeatOfficeResponse>> getOfficeSeatInfo(@PathVariable String seatOffice) {

		List<SeatOfficeResponse> responses = seatService.getOfficeSeatInfo(seatOffice);

		return ApiResponse.createSuccess(responses);
	}

	@GetMapping("/seat/office/{seatOffice}/{floor}")
	public ApiResponse<List<SeatOfficeFloorResponse>> getOfficeFloorSeatInfo(@PathVariable String seatOffice,@PathVariable String floor) {

		List<SeatOfficeFloorResponse> responses = seatService.getOfficeFloorSeatInfo(seatOffice,floor);

		return ApiResponse.createSuccess(responses);
	}

	@GetMapping("/seat/info/{seatStatusNo}")
	public ApiResponse<SeatUsingUserResponse> getSeatUsingUserInfo(@PathVariable Long seatStatusNo) {

		SeatUsingUserResponse response = seatService.getSeatUsingUserInfo(seatStatusNo);

		return ApiResponse.createSuccess(response);
	}

	@GetMapping("/seat/availableseats/{seatOffice}")
	public ApiResponse<List<SeatOfficeResponse>> getAvailableSeats(@PathVariable String seatOffice) {
		List<SeatOfficeResponse> responses = seatService.getAvailableSeats(seatOffice);

		return ApiResponse.createSuccess(responses);
	}

	@PostMapping("/seat/availableseats/{seatOffice}/select/{seatNo}/{userNo}")
	public ApiResponse<String> selectSeat(@PathVariable String seatOffice, @PathVariable Long seatNo,
		@PathVariable Long userNo) {
		seatService.selectSeat(seatNo, userNo, seatOffice);

		return ApiResponse.createSuccessWithMessage(null, SeatMessageConstants.SEAT_SELECT_SUCCESS);
	}

	@PostMapping("/seat/availableseats/{seatOffice}/cancel/{seatNo}/{userNo}")
	public ApiResponse<String> cancelSeat(@PathVariable String seatOffice, @PathVariable Long seatNo,
		@PathVariable Long userNo) {
		seatService.cancelSeat(seatNo, userNo, seatOffice);

		return ApiResponse.createSuccessWithMessage(null, SeatMessageConstants.SEAT_CANCEL_SUCCESS);
	}

	@GetMapping("seat/checkduplicateseatselection/{seatOffice}/select/{seatNo}/{userNo}")
	public ResponseEntity<Boolean> checkDuplicateSeatSelection(@PathVariable String seatOffice,
		@PathVariable Long seatNo, @PathVariable Long userNo) {
		boolean isDuplicate = seatService.checkDuplicateSeatSelection(userNo);
		return ResponseEntity.ok(isDuplicate);
	}
}
