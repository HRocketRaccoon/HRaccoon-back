package org.finalpjt.hraccoon.domain.attendance.service;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.hibernate.grammars.hql.HqlParser.LocalDateTimeContext;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final User user;
    private final Attendance attendance;

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
        return null;
    }


    @Transactional
    public User findByUserNo(Long userNo) {
        return userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public int calculateWeeklyHours(Long userNo) {
        // 금주의 총 근무 시간을 계산하는 로직
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
        int totalHours = attendances.stream()
                .mapToInt(a -> a.getAttendanceTotalTime().toLocalTime().getHour())
                .sum();

        return totalHours;
    }

    public int calculateMonthlyHours(Long userNo, LocalDate date) {
        // 월의 총 근무 시간을 계산하는 로직
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfMonth, endOfMonth);
        int totalHours = attendances.stream()
                .mapToInt(a -> a.getAttendanceTotalTime().toLocalTime().getHour())
                .sum();
        return totalHours;
    }

    public List<Attendance> getWeeklyAttendance(Long userNo) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
    }

    public List<Attendance> getDailyAttendance(Long userNo) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
    }

    public Attendance getAttendanceByDate(Long userNo, LocalDate attendanceDate) {
        return attendanceRepository.findByUserNoAndDate(userNo, attendanceDate)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found for given date"));
    }

}
