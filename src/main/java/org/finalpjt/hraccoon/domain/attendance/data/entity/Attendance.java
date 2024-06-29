package org.finalpjt.hraccoon.domain.attendance.data.entity;

import java.time.Duration;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

	// 요일 표기 추가
	@Column(name = "attendance_day")
	private String attendanceDay;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_no", nullable = false)
	@JsonIgnore
	private User user;

	public void setAttendanceDay(String attendanceDay) {
		this.attendanceDay = attendanceDay;
	}

	public void setAttendanceTotalTime() {
		if (attendanceStartTime != null && attendanceEndTime != null) {
			Duration duration = Duration.between(attendanceStartTime, attendanceEndTime);
			long seconds = duration.getSeconds();
			long hours = seconds / 3600;
			long minutes = (seconds % 3600) / 60;
			long remainingSeconds = seconds % 60;
			this.attendanceTotalTime = LocalTime.of((int)hours, (int)minutes, (int)remainingSeconds);
		} else {
			this.attendanceTotalTime = null;
		}
	}

	@Builder
	public Attendance(Long attendanceNo, LocalDate attendanceDate, LocalDateTime attendanceStartTime,
		LocalDateTime attendanceEndTime,
		LocalTime attendanceTotalTime, String attendanceStatus, String attendanceDay, User user) {
		this.attendanceNo = attendanceNo;
		this.attendanceDate = attendanceDate;
		this.attendanceStartTime = attendanceStartTime;
		this.attendanceEndTime = attendanceEndTime;
		this.attendanceTotalTime = attendanceTotalTime;
		this.attendanceStatus = attendanceStatus;
		this.attendanceDay = attendanceDay;
		this.user = user;
	}

	// 결재 승인 시 근태 상태 변경
	public void updateAttendance(Approval approval) {
		this.attendanceTotalTime = LocalTime.of(8, 0, 0);
		this.attendanceStatus = approval.getApprovalType().toString();
	}

	// 6시 이후 퇴근 시 근태 상태 변경
	public void updateAttendanceEndTime(LocalDateTime attendanceEndTime) {
		this.attendanceEndTime = attendanceEndTime;
		this.attendanceStatus = "퇴근";
		this.setAttendanceTotalTime();
	}

	// test용
	@Builder
	private Attendance(Long attendanceNo) {
		this.attendanceNo = attendanceNo;
	}

}
