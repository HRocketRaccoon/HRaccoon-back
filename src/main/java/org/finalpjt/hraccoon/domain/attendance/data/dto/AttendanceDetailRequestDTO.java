package org.finalpjt.hraccoon.domain.attendance.data.dto;

import jakarta.persistence.Column;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
public class AttendanceDetailRequestDTO {

    private Long attendanceDetailNo;
	
	private Time attendanceDetailStartTime;
	private Time attendanceDetailEndTime;
	private Time attendanceDetailTotalTime;
    
}
