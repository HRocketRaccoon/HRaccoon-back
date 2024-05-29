package org.finalpjt.hraccoon.domain.attendance.data.dto.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Builder
public class AttendanceRequestDTO {

    private Long attendanceDetailNo;
	
	private Time attendanceDetailStartTime;
	private Time attendanceDetailEndTime;
	private Time attendanceDetailTotalTime;
    
}
