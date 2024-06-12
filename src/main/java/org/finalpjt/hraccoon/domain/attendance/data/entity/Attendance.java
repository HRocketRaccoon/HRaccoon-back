package org.finalpjt.hraccoon.domain.attendance.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.global.abstracts.BaseTimeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Attendance extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceNo;

	@Column(name = "attendance_date", nullable = false)
	private LocalDate attendanceDate;

	@Column(name = "attendance_start_time")
	private LocalDateTime attendanceStartTime;

	@Column(name = "attendance_end_time")
	private LocalDateTime attendanceEndTime;

	@Column(name = "attendance_total_time")
	private LocalTime attendanceTotalTime;

	@Column(name = "attendance_status", nullable = false)
	private String attendanceStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_no", nullable = false)
	@JsonIgnore
	private User user;

	@Builder
	public Attendance(LocalDate attendanceDate, LocalDateTime attendanceStartTime, LocalDateTime attendanceEndTime,
		LocalTime attendanceTotalTime, String attendanceStatus, User user) {
		this.attendanceDate = attendanceDate;
		this.attendanceStartTime = attendanceStartTime;
		this.attendanceEndTime = attendanceEndTime;
		this.attendanceTotalTime = attendanceTotalTime;
		this.attendanceStatus = attendanceStatus;
		this.user = user;
	}

	public void updateAttendance(Approval approval) {
		this.attendanceTotalTime = LocalTime.of(8, 0, 0);
		this.attendanceStatus = approval.getApprovalType().toString();
	}
}
