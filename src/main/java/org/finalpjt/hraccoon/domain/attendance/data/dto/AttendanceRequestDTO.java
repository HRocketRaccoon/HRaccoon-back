package org.finalpjt.hraccoon.domain.attendance.data.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Builder
public class AttendanceRequestDTO {

    private Long attendanceDetailNo;
	
	private Time attendanceStartTime;
	private Time attendanceEndTime;
	private Time attendanceTotalTime;
    
}
