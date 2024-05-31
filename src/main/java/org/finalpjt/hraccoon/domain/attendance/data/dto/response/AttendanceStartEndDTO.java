package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceStartEndDTO {
    
    private Long attendanceNo;
    private LocalDate attendanceDate;
    private String attendanceStartTime;
    private String attendanceEndTime;
    private LocalTime attendanceTotalTime;
    private String attendanceStatus;
    private Long userNo;
    private String userName;


}
