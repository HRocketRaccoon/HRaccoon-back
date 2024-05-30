package org.finalpjt.hraccoon.domain.attendance.data.entity;

import java.time.LocalDateTime;

import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.global.abstracts.BaseTimeEntity;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceNo;
	
	@Column(name = "attendance_date", nullable = false)
	private LocalDateTime attendanceDate;

	@Column(name = "attendance_start_time", nullable = false)
	private LocalDateTime attendanceStartTime;

	@Column(name = "attendance_end_time")
	private LocalDateTime attendanceEndTime;

	@Column(name = "attendance_total_time")
	private LocalDateTime attendanceTotalTime;

	@Column(name = "attendance_status", nullable = false)
	private String attendanceStatus;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_no", nullable = false)
	private User user;
}
