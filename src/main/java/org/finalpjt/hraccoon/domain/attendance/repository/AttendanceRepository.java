package org.finalpjt.hraccoon.domain.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.data.entity.AttendanceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceDetail, Long>{

    public Attendance findByUserNo(Long userNo);

    @Modifying
    @Query("SELECT a FROM AttendanceDetail a WHERE a.attendanceDetailDate = :attendanceDetailDate   ")
    public List<AttendanceDetail> getAttendanceDetailByDate(LocalDate date);

    @Modifying
    @Query("SELECT a FROM AttendanceDetail a WHERE a.attendanceDetailDate = :attendanceDetailDate AND a.user_no = :user_no ")
    public AttendanceDetail startend(String attendanceDetailDate, String user_no);
    



}
