package org.finalpjt.hraccoon.domain.attendance.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    // @Modifying
    // @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :attendanceDate   ")
    // public List<Attendance> getAttendanceByDate(LocalDate date);

    @Modifying
    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :attendanceDate AND a.user_no = :user_no ")
    public Attendance startend(String attendanceDate, String user_no);
    

    

    @Query("SELECT a FROM Attendance a WHERE a.user.userNo = :userNo")
    Optional<Attendance> findByUserNo(Long userNo);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDetailDate BETWEEN :startDate AND :endDate AND a.user.userNo = :userNo")
    List<Attendance> findByUserNoAndDateBetween(Long userNo, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDetailDate = :attendanceDate AND a.user.userNo = :userNo")
    Optional<Attendance> findByUserNoAndDate(Long userNo, LocalDate attendanceDate);


}
