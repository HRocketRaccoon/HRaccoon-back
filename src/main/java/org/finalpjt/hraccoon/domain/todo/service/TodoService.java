package org.finalpjt.hraccoon.domain.todo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.finalpjt.hraccoon.domain.todo.data.dto.TodoRequestDTO;
import org.finalpjt.hraccoon.domain.todo.data.dto.TodoResponseDTO;
import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.todo.repository.TodoRepository;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public List<TodoResponseDTO> findByUserNo(Long userNo) {
        User user = userRepository.findByUserNo(userNo);
        if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디를 가진 유저를 찾지 못했습니다 : " + userNo);
		}
		List<Todo> todoList = todoRepository.findAllByUserNo(user).stream()
			.filter(todo -> !todo.getTodoDeleteYn())
			.collect(Collectors.toList());
            
        List<TodoResponseDTO> responseList = TodoResponseDTO.toDtoList(todoList);
        responseList.forEach(todo -> todo.setUserInfo(user));
        return responseList;
    }


    @Transactional
    public TodoResponseDTO saveTodo(TodoRequestDTO data) {
        User user = userRepository.findByUserNo(data.getUserNo());
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디를 가진 유저를 찾지 못했습니다 : " + data.getUserNo());
		}

		Todo todo = data.toEntity(data, user);
		todoRepository.save(todo);

		// 예외 처리
		Todo response = todoRepository.findByTodoNo(todo.getTodoNo());
		if (response == null) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "항목을 생성하는데 실패했습니다.");
		}

        TodoResponseDTO responseDTO = new TodoResponseDTO(response);
		responseDTO.setUserInfo(user);
        return responseDTO;
    }


	public void completeTodo(Long todoNo) {
		Todo todo = todoRepository.findByTodoNo(todoNo);

		if (todo == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디를 가진 할일을 찾지 못했습니다 : " + todoNo);
		}

		todo.updateTodoCompleteYn();
		todoRepository.save(todo);
	}


}

