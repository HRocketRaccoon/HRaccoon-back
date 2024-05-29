package org.finalpjt.hraccoon.domain.attendance.ctrl;

import org.finalpjt.hraccoon.domain.attendance.data.dto.request.AttendanceRequestDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.AttendanceDetail;
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
    @GetMapping("/attendance/weektimepercent")
    public String totalTime(@PathVariable Long userNo) {
        User user = AttendanceService.findByUserNo(userNo);
        return null;
    }
    
    // 사용자는 홈페이지에서 바로 근태 관리 페이지로 이동할 수 있다.
    @GetMapping("/home")
    public String moveToAttendance(@RequestParam User userNo) {
        return null;
    }
    
    // 사용자는 근태관리 페이지에서 금주 총 근무 시간을 확인할 수 있다.
    @GetMapping("/attendance/worktimeperweek")
    public List<AttendanceDetail> workTimePerWeek(@RequestParam String param) {
        return null;
    }    
    
    // 사용자는 금주 근무일별 총 근무 시간을 확인할 수 있다 (해당 주 전체)
    // 단, 일정이 있는 경우 해당일정을 기재한다.
    @GetMapping("/attendance/worktimeperdate/{}")
    public List<AttendanceDetail> workTimePerDay() {
        
        return null;
    }
    
    // erd 다시 얘기해봐야함
    // 사용자는 캘린더를 통해 특정 날짜의 출퇴근 시간을 확인할 수 있다.
    // 2024-05-24
    @GetMapping("/attendance/startend/{user_no}/{attendanceDetailDate}")
    public AttendanceDetail attendacneTimePerDate(@PathVariable("attendanceDetailDate") String attendanceDetailDate, @PathVariable("user_no") String user_no) {
        AttendanceDetail response = attendanceService.startend(attendanceDetailDate, user_no);
        return response;
    }
    

    
    
}
