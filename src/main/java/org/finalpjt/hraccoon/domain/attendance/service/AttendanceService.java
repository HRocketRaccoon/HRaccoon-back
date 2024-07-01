package org.finalpjt.hraccoon.domain.attendance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.user.constant.UserMessageConstants;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);
	private final AttendanceRepository attendanceRepository;
	private final ApprovalRepository approvalRepository;
	private final UserRepository userRepository;

	// 금주 근무시간, 달성률 조회
	public AttendacneWeekPercentResponseDTO calculateWeeklyHours(Long userNo) {
		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
		LocalDate endOfWeek = startOfWeek.plusDays(6);

		List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
		attendances.forEach(System.out::print);

		Integer workedDaysCount = calculateWorkedDays(attendances);

		Integer totalHours = 0;
		for (Attendance attendance : attendances) {
			if (!attendance.getAttendanceStatus().equals("출근")) {
				Duration duration = Duration.between(attendance.getAttendanceStartTime(),
					attendance.getAttendanceEndTime());
				totalHours += duration.toHoursPart();
			}
		}

		// 주 40시간 기준으로 
		double percent = workedDaysCount > 0 ? ((double)totalHours / 40) * 100 : 0;
		AttendacneWeekPercentResponseDTO response = new AttendacneWeekPercentResponseDTO();
		response.of(totalHours, percent);

		return response;
	}

	// 삭제
	public Integer calculateWorkedDays(List<Attendance> attendances) {
		Integer workedDaysCount = 0;
		Set<LocalDate> workedDays = new HashSet<>();

		for (Attendance attendance : attendances) {
			if (!attendance.getAttendanceStatus().equals("출근")) {
				workedDays.add(attendance.getAttendanceDate());
			}
		}
		workedDaysCount = workedDays.size();

		return workedDaysCount;
	} // 직원이 근무한 날짜 수

	// 해당 월 근무시간, 달성률 조회
	public AttendacneMonthPercentResponseDTO calculateMonthlyHours(Long userNo, LocalDate date) {
		LocalDate startOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date;
		// LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

		List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfMonth,
			endOfMonth);

		Integer totalWorkHours = 0;
		for (Attendance attendance : attendances) {
			if (!attendance.getAttendanceStatus().equals("출근")) {
				Duration duration = Duration.between(attendance.getAttendanceStartTime(),
					attendance.getAttendanceEndTime());
				totalWorkHours += duration.toHoursPart();
			}
		}
		// workedDaysCount-> 내가 근무한 날짜 수(주말 제외, 출석 제외), 달 총 근무일자 기준(ex-20일) 정해서 계산
		Integer workedDaysCount = calculateWorkedDays(attendances);
		Integer totalHours = 8 * workedDaysCount;
		double percent = workedDaysCount > 0 ? ((double)totalWorkHours / totalHours) * 100 : 0;

		AttendacneMonthPercentResponseDTO response = new AttendacneMonthPercentResponseDTO();
		response.of(totalHours, totalWorkHours, percent);

		return response;
	}

	// 해당 주 요일별 근무 시간 조회
	public List<Attendance> getDailyAttendance(Long userNo) {
		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
		LocalDate endOfWeek = startOfWeek.plusDays(6);

		User user = userRepository.findByUserNo(userNo)
			.orElseThrow(() -> new IllegalArgumentException(UserMessageConstants.USER_NOT_FOUND));

		List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);

		long fakeAttendanceNo = response.stream()
			.mapToLong(Attendance::getAttendanceNo)
			.max()
			.orElse(0L);

		boolean whetherIncludingToday = true;
		for (Attendance attendance : response) {
			if (attendance.getAttendanceDate().isEqual(today) && !attendance.getAttendanceStatus().equals("출근")
				|| attendance.getAttendanceStatus().equals("BUSINESS_TRIP") || attendance.getAttendanceStatus()
				.equals("OUT_ON_BUSINESS") || attendance.getAttendanceStatus().equals("VACATION")) {
				whetherIncludingToday = false;
				break;
			}
		}

		List<Attendance> fakeAttendances = new ArrayList<>();

		if (whetherIncludingToday) {
			for (LocalDate date = today; date.isBefore(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
				Attendance fakeAttendance = Attendance.builder()
					.attendanceNo(++fakeAttendanceNo)
					.user(user)
					.attendanceDate(date)
					.attendanceStartTime(null)
					.attendanceEndTime(null)
					.attendanceTotalTime(null)
					.attendanceStatus("proxy")
					.attendanceDay(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN))
					.build();

				fakeAttendances.add(fakeAttendance);
			}
		} else {
			for (LocalDate date = today.plusDays(1); date.isBefore(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
				Attendance fakeAttendance = Attendance.builder()
					.attendanceNo(++fakeAttendanceNo)
					.user(user)
					.attendanceDate(date)
					.attendanceStartTime(null)
					.attendanceEndTime(null)
					.attendanceTotalTime(null)
					.attendanceStatus("proxy")
					.attendanceDay(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN))
					.build();

				fakeAttendances.add(fakeAttendance);
			}
		}

		// 요일 설정
		List<Attendance> responseWithDetails = response.stream()
			.peek(attendance -> {
				if (!attendance.getAttendanceStatus().equals("proxy")) {
					attendance.setAttendanceDay(
						attendance.getAttendanceDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
					attendance.setAttendanceTotalTime();
				}
			})
			.collect(Collectors.toList());

		responseWithDetails.addAll(fakeAttendances);

		attendanceRepository.saveAllAndFlush(responseWithDetails.stream()
			.filter(attendance -> !attendance.getAttendanceStatus().equals("proxy"))
			.collect(Collectors.toList()));

		return responseWithDetails;
	}

	// 특정 일자 출퇴근 시간 조회
	public Attendance startend(String attendanceDate, String userNo) {
		LocalDate date = LocalDate.parse(attendanceDate);
		Attendance start = attendanceRepository.startend(date, userNo);

		return start;
	}

	@Transactional
	public void updateAttendance(Long approvalNo) {
		Optional<Approval> approvalOptional = approvalRepository.findById(approvalNo);

		if (approvalOptional.isPresent()) {
			Approval approval = approvalOptional.get();

			if (approval.getApprovalStatus() == ApprovalStatus.APPROVED) {
				LocalDateTime startDate = approval.getApprovalDetail().getApprovalDetailStartDate();
				LocalDateTime endDate = approval.getApprovalDetail().getApprovalDetailEndDate();
				List<LocalDate> dates = getDatesBetween(startDate.toLocalDate(), endDate.toLocalDate());

				for (LocalDate date : dates) {
					Attendance attendance = Attendance.builder()
						.attendanceDate(date)
						.user(approval.getUser())
						.build();

					attendance.updateAttendance(approval);
					attendanceRepository.save(attendance);
				}
			}
		}
	}

	private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = startDate;

		while (!currentDate.isAfter(endDate)) {
			dates.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}

		return dates;
	}

	public List<Attendance> findAttendancesByUserAndDate(User user, LocalDate attendanceDate) {
		return attendanceRepository.findByUserAndAttendanceDate(user, attendanceDate);
	}

	public List<Attendance> findByAttendanceDate(LocalDate today) {
		return attendanceRepository.findByAttendanceDate(today);
	}

	public void delete(Attendance attendance) {
		attendanceRepository.delete(attendance);
	}

}