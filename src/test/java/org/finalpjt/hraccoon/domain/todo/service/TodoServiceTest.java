package org.finalpjt.hraccoon.domain.todo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.finalpjt.hraccoon.domain.todo.data.dto.TodoRequestDTO;
import org.finalpjt.hraccoon.domain.todo.data.dto.TodoResponseDTO;
import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.todo.repository.TodoRepository;
import org.finalpjt.hraccoon.domain.user.data.dto.request.UserRequest;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.finalpjt.hraccoon.domain.user.data.enums.Gender;
import org.finalpjt.hraccoon.domain.user.data.enums.Role;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.finalpjt.hraccoon.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@SpringBootTest
class TodoServiceTest {

	@Autowired
	private TodoRepository todoRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private TodoService todoService;

	@BeforeEach
	void init() {
		// fixture
		UserDetail userDetail1 = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();
		UserDetail userDetail2 = UserDetail.builder()
			.userJoinDate(LocalDateTime.now())
			.userLeavingDate(null)
			.userLeavingReason(null)
			.userRemainVacation(null)
			.build();

		User user1 = User.builder()
			.userId("A000001")
			.userPassword("password12!")
			.userName("방채원")
			.userMobile("010-1234-5678")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("FEMALE"))
			.userBirth("020308")
			.userEmail("glgl246@gmail.com")
			.userDepartment("DP001")
			.userPosition("PS003")
			.userTeam("TM001")
			.userRank("RK007")
			.userRole(Role.valueOf("USER"))
			.build();
		User user2 = User.builder()
			.userId("A000002")
			.userPassword("password13!")
			.userName("이윤재")
			.userMobile("010-1234-5679")
			.userAddress("서울 강남구")
			.userGender(Gender.valueOf("FEMALE"))
			.userBirth("000106")
			.userEmail("avv234@naver.com")
			.userDepartment("DP001")
			.userPosition("PS002")
			.userTeam("TM001")
			.userRank("RK006")
			.userRole(Role.valueOf("USER"))
			.build();
		user1.updateUserDetail(userDetail1);
		user2.updateUserDetail(userDetail2);

		userService.createUser(new UserRequest(user1));
		userService.createUser(new UserRequest(user2));

		Todo todo1 = Todo.builder()
			.user(userRepository.findByUserId("A000001").get())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		Todo todo2 = Todo.builder()
			.user(userRepository.findByUserId("A000001").get())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		Todo todo3 = Todo.builder()
			.user(userRepository.findByUserId("A000002").get())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		todoRepository.save(todo1);
		todoRepository.save(todo2);
		todoRepository.save(todo3);

	}

	@Test
	@DisplayName("특정 사용자의 Todo 전체 조회 할 수 있다.")
	void findByUserNo() {
		// given
		// when
		List<TodoResponseDTO> todoResponses = todoService.findByUserNo(userRepository.findByUserId("A000001").get().getUserNo());
		// then
		assertEquals(2, todoResponses.size());
	}

	@Test
	@DisplayName("존재하지 않는 유저 No로 Todo 전체 조회 할 경우 예외 발생")
	void findByUserNo_exception() {
		// given
		Long nonExistentUserNo = 999L;
		// when
		// then
		assertThrows(IllegalArgumentException.class, () -> {
			todoService.findByUserNo(nonExistentUserNo);
		});
	}

	@Test
	@DisplayName("Todo 저장 할 수 있다.")
	void saveTodo() {
		// given
		TodoRequestDTO todoRequestDTO = TodoRequestDTO.builder()
			.userNo(userRepository.findByUserId("A000001").get().getUserNo())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		// when
		TodoResponseDTO savedTodo = todoService.saveTodo(todoRequestDTO);
		// then
		assertEquals(todoRequestDTO.getTodoContent(), savedTodo.getTodoContent());
	}

	@Test
	@DisplayName("존재하지 않는 유저 No로 Todo 저장 할 경우 예외 발생")
	void saveTodo_exception_user() {
		// given
		TodoRequestDTO todoRequestDTO = TodoRequestDTO.builder()
			.userNo(999L)
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		// when
		// then
		assertThrows(IllegalArgumentException.class, () -> {
			todoService.saveTodo(todoRequestDTO);
		});
	}

	@Test
	@DisplayName("Todo 저장 할 때 할 일을 기입하지 않으면 예외 발생")
	void saveTodo_exception() {
		// given
		TodoRequestDTO todoRequestDTO = TodoRequestDTO.builder()
			.userNo(userRepository.findByUserId("A000001").get().getUserNo())
			.todoContent(null)
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		// when
		// then
		assertThrows(ResponseStatusException.class, () -> {
			todoService.saveTodo(todoRequestDTO);
		});
	}

	@Test
	@DisplayName("Todo complete 수정 할 수 있다.")
	void completeTodo() {
		// given
		Todo todo = Todo.builder()
			.user(userRepository.findByUserId("A000001").get())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		todoRepository.save(todo);
		// when
		todoService.completeTodo(todo.getTodoNo());
		// then
		assertTrue(todo.getTodoCompleteYn());
	}

	@Test
	@DisplayName("존재하지 않는 TodoNo로 Todo complete 수정 할 경우 예외 발생")
	void completeTodo_exception() {
		// given
		Long nonExistentTodoNo = 999L;
		// when
		// then
		assertThrows(ResponseStatusException.class, () -> {
			todoService.completeTodo(nonExistentTodoNo);
		});
	}

	@Test
	@DisplayName("Todo 삭제 할 수 있다.")
	void deleteByTodoNo() {
		// given
		Todo todo = Todo.builder()
			.user(userRepository.findByUserId("A000001").get())
			.todoContent("test")
			.todoCompleteYn(false)
			.todoDeleteYn(false)
			.build();
		todoRepository.save(todo);
		// when
		todoService.deleteByTodoNo(todo.getTodoNo());
		// then
		assertTrue(todo.getTodoDeleteYn());
	}

	@Test
	@DisplayName("존재하지 않는 TodoNo로 Todo 삭제 할 경우 예외 발생")
	void deleteByTodoNo_exception() {
		// given
		Long nonExistentTodoNo = 999L;
		// when
		// then
		assertThrows(ResponseStatusException.class, () -> {
			todoService.deleteByTodoNo(nonExistentTodoNo);
		});
	}
}