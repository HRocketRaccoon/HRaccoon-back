package org.finalpjt.hraccoon.domain.todo.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.finalpjt.hraccoon.domain.todo.data.dto.TodoRequestDTO;
import org.finalpjt.hraccoon.domain.todo.data.dto.TodoResponseDTO;
import org.finalpjt.hraccoon.domain.todo.service.TodoService;
import org.finalpjt.hraccoon.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Valid
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    //등록창에 값을 입력 후 생성 버튼을 클릭 시, 할 일 목록에 새로 생성한 항목이 나타난다.
    @PostMapping("/todo/create")
    public ApiResponse<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO params) {
        TodoResponseDTO dto = todoService.saveTodo(params);
        
        return ApiResponse.createSuccess(dto);
    }
    
    // 홈에서 할 일 버튼을 누르면 할 일 목록을 조회 할 수 있는 화면이 나타난다.
    // 기존에 할 일을 생성하지 않은 경우, 빈 화면을 나타낸다
    @GetMapping("/todo/list/{userNo}")
    public ApiResponse<List<TodoResponseDTO>> readTodoList(@PathVariable Long userNo) {
        List<TodoResponseDTO> response = todoService.findByUserNo(userNo);

        return ApiResponse.createSuccess(response);
    }

	@PostMapping("/todo/complete/{todoNo}")
	public ApiResponse<?> completeTodo(@PathVariable Long todoNo) {
		todoService.completeTodo(todoNo);

		return ApiResponse.createSuccessWithNoContent();
	}
    

    
}
