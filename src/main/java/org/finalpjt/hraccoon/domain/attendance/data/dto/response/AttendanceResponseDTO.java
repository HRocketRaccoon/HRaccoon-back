package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceResponseDTO {

	@NotNull
    private Long attendanceNo;
	private LocalDateTime attendanceDate;
	private LocalDateTime attendanceStartTime;
	private LocalDateTime attendanceEndTime;
	private LocalDateTime attendanceTotalTime;
	private String attendanceStatus;



}
