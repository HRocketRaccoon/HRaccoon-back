package org.finalpjt.hraccoon.domain.attendance.ctrl;

import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.service.AttendanceService;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 사용자는 홈페이지에서 자신의 해당 주 근무 시간과 달성률을 확인할 수 있다.
    @GetMapping("/attendance/weektotalpercent/{userNo}")
    public ApiResponse<AttendacneWeekPercentResponseDTO> totalTime(@PathVariable Long userNo) {
        AttendacneWeekPercentResponseDTO response = attendanceService.calculateWeeklyHours(userNo);

        return ApiResponse.createSuccess(response);
    }

    // 사용자는 홈페이지에서 자신의 해당 달 근무 시간과 달성률을 확인할 수 있다.
    // 월 기준 조회일자 전날까지의 근무 시간 퍼센트로 확인 가능
    // 1일 ~ 조회일자 전날까지 합계를 월 기준 근무시간
    // 29일->21일, 168시간 / 30일->22일 176시간/ 31일->23일, 184시간 (rhals)
    @GetMapping("/attendance/monthtotalpercent/{userNo}")
    public ApiResponse<AttendacneMonthPercentResponseDTO> getMonthlyTotal(@PathVariable Long userNo) {
        AttendacneMonthPercentResponseDTO response = attendanceService.calculateMonthlyHours(userNo, LocalDate.now());

        return ApiResponse.createSuccess(response);
    }
    
    // 보류
    // 사용자는 홈페이지에서 바로 근태 관리 페이지로 이동할 수 있다.
    @GetMapping("/attendance")
    public String moveToAttendance(@RequestParam User userNo) {
        // 캘린더, 퍼센트, 일별 출퇴근시간 한번에 어케 넘길것인지
        return null;
    }
    
    // 사용자는 금주 근무일별 총 근무 시간을 확인할 수 있다 (해당 주 전체)
    // 단, 일정이 있는 경우 해당일정이 기재된다.
    @GetMapping("/attendance/worktimeperdate/{userNo}")
    public ApiResponse<List<Attendance>> workTimePerDay(@PathVariable Long userNo) {
        List<Attendance> response = attendanceService.getDailyAttendance(userNo);

        return ApiResponse.createSuccess(response);
    }
    
    // 사용자는 캘린더를 통해 특정 날짜의 출퇴근 시간을 확인할 수 있다.
    // 2024-05-24
    @GetMapping("/attendance/startend/{user_no}/{attendanceDate}")
    public ApiResponse<Attendance> attendacneTimePerDate(@PathVariable("user_no") String user_no, @PathVariable("attendanceDate") String attendanceDate ) {
        System.out.println("debug >>> ctrl attendanceDate params , "+attendanceDate);
        System.out.println("debug >>> ctrl user_no params , "+user_no);
        

        Attendance response = attendanceService.startend(attendanceDate, user_no);
        System.out.println("debug >>> ctrl response , "+response);

        return ApiResponse.createSuccess(response);
    }
    

    
    
}