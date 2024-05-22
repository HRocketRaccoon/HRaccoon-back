package org.finalpjt.hraccoon.domain.attendance.data.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_detail_no")
	private Long attendanceDetailNo;

	@CreationTimestamp
	@Column(name = "attendance_detail_date", nullable = false)
	private LocalDateTime attendanceDetailDate;

	@CreationTimestamp
	@Column(name = "attendance_detail_start_time", nullable = false)
	private LocalDateTime attendanceDetailStartTime;

	@Column(name = "attendance_detail_end_time")
	private LocalDateTime attendanceDetailEndTime;

	@Column(name = "attendance_detail_total_time")
	private LocalDateTime attendanceDetailTotalTime;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "attendance_no", nullable = false)
	private Attendance attendance;
}
