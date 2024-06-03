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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    // Todo : 특정 일자의 출/퇴근 시간을 조회하는 로직
    public Attendance startend(String attendanceDate, String userNo) {
        LocalDate date = LocalDate.parse(attendanceDate);
        Attendance start = attendanceRepository.startend(date, userNo);
        System.out.println("debug result :: "+start); 
        return start;
    }

    // Todo : 특정 주의 근무 일수를 구하는 로직
    public int calculateWorkedDays(List<Attendance> attendances) {
        int workedDaysCount = 0;
        Set<LocalDate> workedDays = new HashSet<>();
    
        for (Attendance attendance : attendances) {
            if ("출근".equals(attendance.getAttendanceStatus()) || "퇴근".equals(attendance.getAttendanceStatus())) {
                workedDays.add(attendance.getAttendanceDate());
                System.out.println("출퇴근 debug >>> " + attendance.getAttendanceStatus());
            } 
        }
    
        workedDaysCount = workedDays.size();
        return workedDaysCount;
    }
    
    // Todo : 금주의 총 근무 시간과 달성률 계산하는 로직
    public AttendacneWeekPercentResponseDTO calculateWeeklyHours(Long userNo) {
        // 평일 구하는 로직
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        System.out.println("service debug >>>> "+startOfWeek+"\t"+endOfWeek);

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek , endOfWeek);
        attendances.forEach(System.out::print); 

        int workedDaysCount = calculateWorkedDays(attendances);

        int totalHours = 0;
        for (Attendance attendance : attendances) {
            if ( "출근".equals(attendance.getAttendanceStatus()) || "퇴근".equals(attendance.getAttendanceStatus()) ) {
                Duration duration = Duration.between(attendance.getAttendanceStartTime(), attendance.getAttendanceEndTime());
                totalHours += duration.toHours();
            }
        }
        System.out.println("service debug >>> totalHours : "+totalHours);

        double percent = workedDaysCount > 0 ? ((double) totalHours / (8 * workedDaysCount)) * 100 : 0;
        AttendacneWeekPercentResponseDTO response = new AttendacneWeekPercentResponseDTO();
        response.of(totalHours, percent);

        return response;
    }

    // Todo : 월의 총 근무 시간과 달성률 계산하는 로직
    public AttendacneMonthPercentResponseDTO calculateMonthlyHours(Long userNo, LocalDate date) {

        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfMonth , endOfMonth);
        int totalHours = attendances.stream()
                .mapToInt(a -> a.getAttendanceTotalTime().getHour())
                .sum();

        int workedDaysCount = calculateWorkedDays(attendances);
        double percent = workedDaysCount > 0 ? ((double) totalHours / (8 * workedDaysCount)) * 100 : 0;
        // double percent = (double) (totalHours / 8*workedDaysCount) * 100; 
        
        AttendacneMonthPercentResponseDTO response = new AttendacneMonthPercentResponseDTO();
        response.of(totalHours, percent);

        return response;
    }

    // Todo : 해당 주의 요일별 근무시간 조회하는 로직
    public List<Attendance> getDailyAttendance(Long userNo) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        System.out.println("debug >>>>> ");
        System.out.println("debug >>>>> "+userNo);
        System.out.println("debug >>>>> "+String.valueOf(startOfWeek));
        System.out.println("debug >>>>> "+String.valueOf(endOfWeek));
        
        List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek , endOfWeek);
        
        return response;
    }

    // public Attendance getAttendanceByDate(Long userNo, LocalDate attendanceDate) {
    //     return attendanceRepository.findByUserNoAndDate(userNo, attendanceDate)
    //             .orElseThrow(() -> new IllegalArgumentException("Attendance not found for given date"));
    // }

}