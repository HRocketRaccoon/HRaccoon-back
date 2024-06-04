package org.finalpjt.hraccoon.domain.attendance.ctrl;

import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.service.AttendanceService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/attendance/weektotalpercent/{userNo}")
    public ApiResponse<AttendacneWeekPercentResponseDTO> totalTime(@PathVariable Long userNo) {
        AttendacneWeekPercentResponseDTO response = attendanceService.calculateWeeklyHours(userNo);

        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/attendance/monthtotalpercent/{userNo}")
    public ApiResponse<AttendacneMonthPercentResponseDTO> getMonthlyTotal(@PathVariable Long userNo) {
        AttendacneMonthPercentResponseDTO response = attendanceService.calculateMonthlyHours(userNo, LocalDate.now());

        return ApiResponse.createSuccess(response);
    }
    
    @GetMapping("/attendance/worktimeperdate/{userNo}")
    public ApiResponse<List<Attendance>> workTimePerDay(@PathVariable Long userNo) {
        List<Attendance> response = attendanceService.getDailyAttendance(userNo);

        return ApiResponse.createSuccess(response);
    }
    
    @GetMapping("/attendance/startend/{user_no}/{attendanceDate}")
    public ApiResponse<Attendance> attendacneTimePerDate(@PathVariable("user_no") String user_no, @PathVariable("attendanceDate") String attendanceDate ) {
        Attendance response = attendanceService.startend(attendanceDate, user_no);

        return ApiResponse.createSuccess(response);
    }
    

    
    
}