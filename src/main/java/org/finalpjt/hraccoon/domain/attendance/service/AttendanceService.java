package org.finalpjt.hraccoon.domain.attendance.service;

import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    // @Transactional
    // // 받아온 userNo를 기반으로 시간,,,계산하려면 다른 것도 받아와야 하지 않나?
    // public static User findByUserNo(Long userNo) {
    //     // User a =attendanceRepository.findByUserNo(userNo); 
    //     return findByUserNo(userNo);
    // }

    // public List<Attendance> getAttendanceDetailByDate(LocalDate date) {
    //     return attendanceRepository.getAttendanceByDate(date);
    // }

    public Attendance startend(String attendanceDate, String user_no) {
        Attendance start = attendanceRepository.startend(attendanceDate, user_no);
        return start;
    }

    public AttendacneWeekPercentResponseDTO calculateWeeklyHours(Long userNo) {
        // 금주의 총 근무 시간을 계산하는 로직
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
        int totalHours = attendances.stream()
                .mapToInt(a -> a.getAttendanceTotalTime().toLocalTime().getHour())
                .sum();

        int workedDaysCount = attendances.size();
        double percent = (double) totalHours / (8*workedDaysCount) * 100;

        AttendacneWeekPercentResponseDTO response = new AttendacneWeekPercentResponseDTO();
        response.of(totalHours, percent);

        // @Setter로 값 넣는 법
        // @RequiredArgsConstructor; -> dto에 걸어주기
        // AttendacneWeekPercentResponseDTO response = new AttendacneWeekPercentResponseDTO();
        // response.setFormattedPercent(percent);
        // response.setTotalWorkHours(totalHours);
        
        return response;
    }


    public AttendacneMonthPercentResponseDTO calculateMonthlyHours(Long userNo, LocalDate date) {
        // 월의 총 근무 시간을 계산하는 로직
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfMonth, endOfMonth);
        int totalHours = attendances.stream()
                .mapToInt(a -> a.getAttendanceTotalTime().toLocalTime().getHour())
                .sum();
        double percent = (double) totalHours / 176 * 100; 
        // 30일(근무일 22일) 기준, 변경 필요함

        AttendacneMonthPercentResponseDTO response = new AttendacneMonthPercentResponseDTO();
        response.of(totalHours, percent);

        return response;
    }

    // public List<Attendance> getWeeklyAttendance(Long userNo) {
    //     LocalDate today = LocalDate.now();
    //     LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
    //     LocalDate endOfWeek = startOfWeek.plusDays(6);
    //     return attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
    // }

    public List<Attendance> getDailyAttendance(Long userNo) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
        
        return response;
    }

    public Attendance getAttendanceByDate(Long userNo, LocalDate attendanceDate) {
        return attendanceRepository.findByUserNoAndDate(userNo, attendanceDate)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found for given date"));
    }

}
