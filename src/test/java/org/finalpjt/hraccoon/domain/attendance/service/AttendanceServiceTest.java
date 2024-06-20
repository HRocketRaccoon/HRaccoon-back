package org.finalpjt.hraccoon.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.repository.AttendanceRepository;
import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.todo.repository.TodoRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class AttendanceServiceTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;


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
        // System.out.println("debug >>> "+hour);
        // System.out.println("debug >>> "+minutes);
        // System.out.println("debug >>> "+seconds);
        
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
        Long userNo = 1L;
        User user = User.builder().userNo(userNo).build();    

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
        assertFalse(savedTodo.getTodoCompleteYn()); // todoCompleteYn false
        assertFalse(savedTodo.getTodoDeleteYn()); // todoDeleteYn false
        assertEquals(user.getUserNo(), savedTodo.getUserNo().getUserNo()); // userNo 일치여부 확인
    }

    @Test
    @DisplayName("userNo 이용하여 할 일 전체 목록 조회할 수 있음")
    void findByUserNo() {
        // given
        Optional<User> user = userRepository.findByUserNo(1L);

        Todo todo1 = Todo.builder()
                .user(user.get())
                .todoNo(1L)
                .todoContent("첫 번째 할 일")
                .todoCompleteYn(false)
                .todoDeleteYn(false)
                .build();
        todoRepository.save(todo1);

        Todo todo2 = Todo.builder()
                .user(user.get())
                .todoNo(2L)
                .todoContent("두 번째 할 일")
                .todoCompleteYn(true)
                .todoDeleteYn(false)
                .build();
        todoRepository.save(todo2);

        // when
        List<Todo> todos = todoRepository.findAllByUserNo(user.get());

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
        Long userNo = 1L;
        User user = User.builder().userNo(userNo).build();        

        // when
        List<Todo> todos = todoRepository.findAllByUserNo(user); 

        // then
        assertEquals(0, todos.size());
    }

    @Test
    @DisplayName("todoNo로 할 일 완료 처리 할 수 있음")
    void completeTodo() {
        // given
        Long todoNo = 1L;
        Long userNo = 1L;
        User user = User.builder().userNo(userNo).build();

        Todo todo = Todo.builder()
            .user(user)
            .todoNo(todoNo)
            .todoContent("첫 번째 할 일")
            .todoCompleteYn(false)
            .todoDeleteYn(false)
            .build();
        todoRepository.save(todo);
        
        // when
        Todo updatedTodo = todoRepository.findByTodoNo(todo.getTodoNo());
        updatedTodo.updateTodoCompleteYn();
        todoRepository.save(updatedTodo);

        // then
        assertNotNull(updatedTodo, "업데이트 되는 값이 널이 아님");
        assertTrue(updatedTodo.getTodoCompleteYn(), "할 일의 todoCompleteYn가 true로 설정됨");
        }

    @Test
    @DisplayName("todoNo로 할 일 삭제 처리 가능")
    void deleteByTodoNo() {
        //given
        Long todoNo = 1L;
        Todo todo = Todo.builder().todoNo(todoNo).build();

        // when
        Todo savedTodo = todoRepository.findByTodoNo(todo.getTodoNo());

        savedTodo.updateTodoDeleteYn();
        todoRepository.save(savedTodo);

        // then
        assertNotNull(savedTodo, "할 일 목록에 존재함");
        assertTrue(savedTodo.getTodoDeleteYn(), "todoDeleteYn 삭제되었다고 표시");
    }

}