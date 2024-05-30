package org.finalpjt.hraccoon.domain.attendance.ctrl;

import org.finalpjt.hraccoon.domain.attendance.data.dto.AttendanceRequestDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.service.AttendanceService;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDate;

import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 사용자는 자신의 금주 근무 시간을 홈페이지에서 퍼센트로 확인할 수 있다.
    @GetMapping("/attendance/totalpercent/{userNo}")
    public String totalTime(@PathVariable Long userNo) {
        User user = attendanceService.findByUserNo(userNo);
        // 로직 작성: 예시로 40시간을 금주의 목표로 잡고 현재 근무 시간을 계산합니다.
        int totalWorkHours = attendanceService.calculateWeeklyHours(userNo);
        int weeklyTargetHours = 40;
        double percent = (double) totalWorkHours / weeklyTargetHours * 100;
        return String.format("%.2f%%", percent);
    }

    // 사용자는 자신의 월 총 근무 시간 달성률을 홈페이지에서 퍼센트로 확인할 수 있다.
    // 월 기준 조회일자 전날까지의 근무 시간 퍼센트로 확인 가능
    // 1일 ~ 조회일자 전날까지 합계를 월 기준 근무시간
    // 29일->21일, 168시간 / 30일->22일 176시간/ 31일->23일, 184시간
    @GetMapping("/attendance/total/{userNo}")
    public String getMonthlyTotal(@PathVariable Long userNo) {
        // 월 기준 근무 시간 계산 로직 구현
        User user = attendanceService.findByUserNo(userNo); // 서비스 메서드 사용
        int totalWorkHours = attendanceService.calculateMonthlyHours(userNo, LocalDate.now());
        int monthlyTargetHours = 168; // 29일 기준, 필요시 동적으로 변경
        double percent = (double) totalWorkHours / monthlyTargetHours * 100;
        return String.format("%.2f%%", percent);
    }
    
    // 사용자는 홈페이지에서 바로 근태 관리 페이지로 이동할 수 있다.
    @GetMapping("/attendance")
    public String moveToAttendance(@RequestParam User userNo) {
        // 캘린더, 퍼센트, 일별 출퇴근시간 한번에 어케 넘길것인지
        return null;
    }
    
    // 사용자는 근태관리 페이지에서 금주 총 근무 시간을 확인할 수 있다.
    @GetMapping("/attendance/worktimeperweek/{userNo}")
    public List<Attendance> workTimePerWeek(@PathVariable Long userNo) {
        // 금주 총 근무 시간을 반환하는 로직 구현
        return attendanceService.getWeeklyAttendance(userNo);
    }    
    
    // 사용자는 금주 근무일별 총 근무 시간을 확인할 수 있다 (해당 주 전체)
    // 단, 일정이 있는 경우 해당일정을 기재한다.
    @GetMapping("/attendance/worktimeperdate/{userNo}")
    public List<Attendance> workTimePerDay(@PathVariable Long userNo) {
        // 금주 근무일별 총 근무 시간을 반환하는 로직 구현
        return attendanceService.getDailyAttendance(userNo);
    }
    
    // erd 다시 얘기해봐야함
    // 사용자는 캘린더를 통해 특정 날짜의 출퇴근 시간을 확인할 수 있다.
    // 2024-05-24
    @GetMapping("/attendance/startend/{user_no}/{attendanceDate}")
    public Attendance attendacneTimePerDate(@PathVariable("attendanceDate") String attendanceDate, @PathVariable("user_no") String user_no) {
        Attendance response = attendanceService.startend(attendanceDate, user_no);
        return response;
    }
    

    
    
}
