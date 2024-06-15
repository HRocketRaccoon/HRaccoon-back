package org.finalpjt.hraccoon.domain.attendance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;
import java.util.Locale;
import java.time.format.TextStyle;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final ApprovalRepository approvalRepository;

	/**
	 * Todo: 근무 상태가 '퇴근'인 경우의 총 근무 시간을 구하는 로직
	 * 중복 코드 간단하게 수정하면 좋을듯함
	 * public Attendance totalHours(Long userNo) {
	 * }
	 */

	public AttendacneWeekPercentResponseDTO calculateWeeklyHours(Long userNo) {
		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
		LocalDate endOfWeek = startOfWeek.plusDays(4);

		List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
		attendances.forEach(System.out::print);

		Integer workedDaysCount = calculateWorkedDays(attendances);

		Integer totalHours = 0;
		for (Attendance attendance : attendances) {
			if ("퇴근".equals(attendance.getAttendanceStatus())) {
				Duration duration = Duration.between(attendance.getAttendanceStartTime(),
					attendance.getAttendanceEndTime());
				totalHours += duration.toHoursPart();
			}
		}

		double percent = workedDaysCount > 0 ? ((double)totalHours / (8 * workedDaysCount)) * 100 : 0;
		AttendacneWeekPercentResponseDTO response = new AttendacneWeekPercentResponseDTO();
		response.of(totalHours, percent);

		return response;
	}

	public AttendacneMonthPercentResponseDTO calculateMonthlyHours(Long userNo, LocalDate date) {
		LocalDate startOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

		List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfMonth,
			endOfMonth);

		Integer totalHours = 0;
		for (Attendance attendance : attendances) {
			if ("퇴근".equals(attendance.getAttendanceStatus())) {
				Duration duration = Duration.between(attendance.getAttendanceStartTime(),
					attendance.getAttendanceEndTime());
				totalHours += duration.toHoursPart();
			}
		}

		Integer workedDaysCount = calculateWorkedDays(attendances);
		double percent = workedDaysCount > 0 ? ((double)totalHours / (8 * workedDaysCount)) * 100 : 0;

		AttendacneMonthPercentResponseDTO response = new AttendacneMonthPercentResponseDTO();
		response.of(totalHours, percent);

		return response;
	}

	public List<Attendance> getDailyAttendance(Long userNo) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek , endOfWeek);
        
        // 요일 설정
        List<Attendance> responseWithDetails = response.stream()
            .peek(attendance -> {
                attendance.setAttendanceDay(attendance.getAttendanceDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
                attendance.setAttendanceTotalTime();
            })
            .collect(Collectors.toList());
            
        attendanceRepository.saveAllAndFlush(responseWithDetails);
        return responseWithDetails;
    }

	public Attendance startend(String attendanceDate, String userNo) {
		LocalDate date = LocalDate.parse(attendanceDate);
		Attendance start = attendanceRepository.startend(date, userNo);

		return start;
	}

	public Integer calculateWorkedDays(List<Attendance> attendances) {
		Integer workedDaysCount = 0;
		Set<LocalDate> workedDays = new HashSet<>();

		for (Attendance attendance : attendances) {
			if ("퇴근".equals(attendance.getAttendanceStatus())) {
				workedDays.add(attendance.getAttendanceDate());
			}
		}
		workedDaysCount = workedDays.size();

		return workedDaysCount;
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

}