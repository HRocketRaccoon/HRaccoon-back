package org.finalpjt.hraccoon.domain.attendance.service;

import org.finalpjt.hraccoon.domain.attendance.data.entity.AttendanceDetail;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
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
    private final AttendanceDetail attendanceDetail;

    private final AttendanceRepository attendanceRepository;

    @Transactional
    // 받아온 userNo를 기반으로 시간,,,계산하려면 다른 것도 받아와야 하지 않나?
    public static User findByUserNo(Long userNo) {
        // User a =attendanceRepository.findByUserNo(userNo); 
        return findByUserNo(userNo);
    }

    public List<AttendanceDetail> getAttendanceDetailByDate(LocalDate date) {
        return attendanceRepository.getAttendanceDetailByDate(date);
    }

    public AttendanceDetail startend(String attendanceDetailDate, String user_no) {
        AttendanceDetail start = attendanceRepository.startend(attendanceDetailDate, user_no);
        return null;
    }

}
