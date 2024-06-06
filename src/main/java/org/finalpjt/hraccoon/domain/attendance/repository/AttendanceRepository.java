package org.finalpjt.hraccoon.domain.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    
    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :attendanceDate AND a.user.userNo = :userNo ")
    public Attendance startend(LocalDate attendanceDate, String userNo);
    
    @Query("SELECT a FROM Attendance a WHERE a.user.userNo = :userNo")
    Optional<Attendance> findByUserNo(Long userNo);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate AND a.user.userNo = :userNo")
    List<Attendance> findByUserNoAndDateBetween(Long userNo, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :attendanceDate AND a.user.userNo = :userNo")
    Optional<Attendance> findByUserNoAndDate(Long userNo, LocalDate attendanceDate);

}