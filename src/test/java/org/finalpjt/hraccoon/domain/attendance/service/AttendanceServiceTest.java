package org.finalpjt.hraccoon.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.entity.ApprovalDetail;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.approval.repository.ApprovalRepository; // 추가된 부분
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneMonthPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.dto.response.AttendacneWeekPercentResponseDTO;
import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.todo.repository.TodoRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.UserDetailRepository;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDetailRepository userDetailRepository;



    @Test
    @DisplayName("userNo으로 주간 총 근무시간을 확일할 수 있음")
    void weektotalpercent() {
        // given
        Long userNo = 1L;
        LocalDate today = LocalDate.of(2024, 6, 5);
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeek);
        response.forEach(System.out::println); 

        // when
        int hour = 0 , minutes = 0 , seconds = 0 ; 
        for(Attendance entity : response) {
            hour += entity.getAttendanceTotalTime().getHour();
            minutes += entity.getAttendanceTotalTime().getMinute();
            seconds += entity.getAttendanceTotalTime().getSecond();
        }
        System.out.println("debug >>> "+hour);
        System.out.println("debug >>> "+minutes);
        System.out.println("debug >>> "+seconds);
        
        // then
        // 3개의 근태 내역이 있음
        // 시.분.초 비어있지 않음
        assertThat(response).hasSize(3);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("해당 주 요일별 근무 시간을 조회할 수 있음")
    void getDailyAttendance() {
        // given
        Long userNo = 1L;
        LocalDate today = LocalDate.of(2024, 5, 6);
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() -1);
        LocalDate endOfWeeek = startOfWeek.plusDays(6);

        List<Attendance> response = attendanceRepository.findByUserNoAndDateBetween(userNo, startOfWeek, endOfWeeek);

        // when
        List<String> totalTimePerDayAndTime = new ArrayList<>();
        for(Attendance entity : response) {
            String day = entity.getAttendanceDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            String totalTime = String.valueOf(entity.getAttendanceTotalTime());
            String status = entity.getAttendanceStatus();
            totalTimePerDayAndTime.add("근무상태: " + status + ", 근무요일: " + day + ", 총 근무시간: " + totalTime + " // ");
        }

        // then
        assertNotNull(response);
        assertThat(totalTimePerDayAndTime).hasSize(5);
    }

    @Test
    @DisplayName("유저 번호와 특정 날짜로 해당 일자의 출퇴근 시간을 조회할 수 있음")
    void startend() {
        // given
        Long userNo = 1L;
        LocalDate attendanceDate = LocalDate.of(2024, 5, 6);     

        // when
        Optional<Attendance> response = attendanceRepository.findByUserNoAndDate(userNo, attendanceDate);
        Attendance attendance = response.get();

        // then
        assertThat(response).isPresent();
        assertThat(attendance.getAttendanceDate()).isEqualTo(attendanceDate);
        assertThat(attendance.getUser().getUserNo()).isEqualTo(userNo);
    }

    // db에 저장되도록 flush 사용, @Transactional만 주석처리시 확인 가능
    @Test
    @DisplayName("할 일 신규 생성할 수 있음")
    void saveTodo() {

        // given
        UserDetail userDetail = UserDetail.builder()
            .userJoinDate(LocalDateTime.now())
            .userRemainVacation(15)
            .build();

            userDetailRepository.saveAndFlush(userDetail);

        User user = User.builder()
            .userNo(301L)
            .userId("A000301")
            .userPassword("password301")
            .userName("션")
            .userMobile("010-5539-2951")
            .userAddress("울산")
            .userGender(Gender.FEMALE)
            .userBirth("1998-09-29")
            .userEmail("sh@naver.com")
            .userDepartment("DP004")
            .userTeam("TM011")
            .userRank("RK005")
            .userPosition("PS000")
            .userRole(Role.USER)
            .userDetail(userDetail)
            .build(); 

            userRepository.saveAndFlush(user);

        Todo todo = Todo.builder()
            .todoNo(1L)
            .user(user)
            .todoContent("안녕")
            .todoCompleteYn(false)
            .todoDeleteYn(false)
            .build();
            
            todoRepository.saveAndFlush(todo);

        // when
        Todo savedTodo = todoRepository.saveAndFlush(todo);

        // then
        assertNotNull(savedTodo); // saveTodo되는 값이 null이 아님
        assertNotNull(savedTodo.getTodoNo()); // Todo가 생성되었고, 그 값이 null이 아님
        assertEquals("안녕", savedTodo.getTodoContent()); // todoContent 내용 일치 확인
        assertFalse(savedTodo.getTodoCompleteYn()); // todoCompleteYn 상태 
        assertFalse(savedTodo.getTodoDeleteYn()); // todoDeleteYn
        assertEquals(user.getUserNo(), savedTodo.getUserNo().getUserNo()); // Verify userNo matches
    }

    @Test
    @DisplayName("userNo 이용하여 할 일 전체 목록 조회할 수 있음")
    void findByUserNo() {
        // given

        User user = User.builder()
                .userNo(1L)
                .userId("test_user")
                .userName("테스트 유저")
                .build();
        userRepository.save(user);

        Todo todo1 = Todo.builder()
                .user(user)
                .todoNo(1L)
                .todoContent("첫 번째 할 일")
                .todoCompleteYn(false)
                .todoDeleteYn(false)
                .build();
        todoRepository.save(todo1);

        Todo todo2 = Todo.builder()
                .user(user)
                .todoNo(2L)
                .todoContent("두 번째 할 일")
                .todoCompleteYn(true)
                .todoDeleteYn(false)
                .build();
        todoRepository.save(todo2);

        // when
        List<Todo> todos = todoRepository.findAllByUserNo(user);

        // then
        assertNotNull(todos); // 조회된 할 일 목록이 null이 아님
        assertEquals(2, todos.size()); // 할 일 목록의 크기가 2여야 함

        Todo foundTodo1 = todos.get(0);
        assertEquals("첫 번째 할 일", foundTodo1.getTodoContent()); // 첫 번째 할 일의 내용 확인
        assertFalse(foundTodo1.getTodoCompleteYn()); // 첫 번째 할 일의 완료 여부 확인

        Todo foundTodo2 = todos.get(1);
        assertEquals("두 번째 할 일", foundTodo2.getTodoContent()); // 두 번째 할 일의 내용 확인
        assertTrue(foundTodo2.getTodoCompleteYn()); // 두 번째 할 일의 완료 여부 확인
    }

    @Test
    @DisplayName("userNo 이용하여 할 일 목록 조회, todo 기입된 내역 없을때도 확인 가능함")
    void findNothingByUserNo() {
        // given
        User user = User.builder()
                .userNo(1L)
                .userId("test_user")
                .userName("테스트 유저")
                .build();
        userRepository.save(user);

        // when
        List<Todo> todos = todoRepository.findAllByUserNo(user);

        // then
        assertEquals(0, todos.size());
    }


        // given

        // when

        // then

    @Test
    @DisplayName("특정 날짜와 유저 번호로 출퇴근 시간을 조회할 수 있다.")
    void startendGPT() {
        // given
        Long userNo = 1L;
        LocalDate attendanceDate = LocalDate.of(2024, 6, 12);
        User user = User.builder().userNo(userNo).build();
        Attendance attendance = Attendance.builder()
            .attendanceDate(attendanceDate)
            .attendanceStartTime(LocalDateTime.of(2024, 6, 12, 9, 0))
            .attendanceEndTime(LocalDateTime.of(2024, 6, 12, 18, 0))
            .user(user)
            .build();
        attendanceRepository.save(attendance);

        // when
        Attendance result = attendanceService.startend(attendanceDate.toString(), userNo.toString());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAttendanceStartTime()).isEqualTo(LocalDateTime.of(2024, 6, 12, 9, 0));
        assertThat(result.getAttendanceEndTime()).isEqualTo(LocalDateTime.of(2024, 6, 12, 18, 0));
    }

    @Test
    @DisplayName("주간 근무 시간을 계산할 수 있다.")
    void calculateWeeklyHours() {
        // given
        Long userNo = 1L;
        User user = User.builder().userNo(userNo).build();
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        for (int i = 0; i < 5; i++) {
            Attendance attendance = Attendance.builder()
                .attendanceDate(startDate.plusDays(i))
                .attendanceStartTime(LocalDateTime.of(2024, 6, 10 + i, 9, 0))
                .attendanceEndTime(LocalDateTime.of(2024, 6, 10 + i, 18, 0))
                .attendanceStatus("퇴근")
                .user(user)
                .build();
            attendanceRepository.save(attendance);
        }

        // when
        AttendacneWeekPercentResponseDTO response = attendanceService.calculateWeeklyHours(userNo);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTotalWorkHours()).isEqualTo(45); // 5일 * 9시간
        assertThat(response.getFormattedPercent()).isEqualTo(112.5); // 45시간 / (8 * 5일) * 100
    }

    @Test
    @DisplayName("월간 근무 시간을 계산할 수 있다.")
    void calculateMonthlyHours() {
        // given
        Long userNo = 123L;
        User user = User.builder().userNo(userNo).build();
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        for (int i = 0; i < 20; i++) {
            Attendance attendance = Attendance.builder()
                .attendanceDate(startDate.plusDays(i))
                .attendanceStartTime(LocalDateTime.of(2024, 6, 1 + i, 9, 0))
                .attendanceEndTime(LocalDateTime.of(2024, 6, 1 + i, 18, 0))
                .attendanceStatus("퇴근")
                .user(user)
                .build();
            attendanceRepository.save(attendance);
        }

        // when
        AttendacneMonthPercentResponseDTO response = attendanceService.calculateMonthlyHours(userNo, startDate);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTotalWorkHours()).isEqualTo(180); // 20일 * 9시간
        assertThat(response.getFormattedPercent()).isEqualTo(112.5); // 180시간 / (8 * 20일) * 100
    }

    @Test
    @DisplayName("승인 정보를 기반으로 출근 정보를 업데이트할 수 있다.")
    void updateAttendance() {
        // given
        Long approvalNo = 1L;
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 14);
        ApprovalDetail approvalDetail = ApprovalDetail.builder()
            .approvalDetailContent("HI")
            .approvalDetailStartDate(startDate.atStartOfDay())
            .approvalDetailEndDate(endDate.atTime(23, 59, 59))
            .build();
        Approval approval = Approval.builder()
            .approvalStatus(ApprovalStatus.APPROVED)
            .approvalDetail(approvalDetail)
            .user(User.builder().userNo(123L).build())
            .build();
        approvalRepository.save(approval);

        // when
        attendanceService.updateAttendance(approvalNo);

        // then
        List<Attendance> attendances = attendanceRepository.findByUserNoAndDateBetween(1L, startDate, endDate);
        assertThat(attendances).hasSize(3);
        for (Attendance attendance : attendances) {
            assertThat(attendance.getAttendanceTotalTime()).isEqualTo(LocalTime.of(20, 45, 42));
            assertThat(attendance.getAttendanceStatus()).isEqualTo(approval.getApprovalStatus().toString());
        }
    }
}