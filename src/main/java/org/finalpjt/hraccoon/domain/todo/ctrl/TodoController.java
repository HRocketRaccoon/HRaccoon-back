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

    @PostMapping("/todo/create")
    public ApiResponse<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO params) {
        TodoResponseDTO dto = todoService.saveTodo(params);
        
        return ApiResponse.createSuccess(dto);
    }
    
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
    
    @PostMapping("/todo/delete/{todoNo}")
    public ApiResponse<?> deleteTodo(@PathVariable Long todoNo){
        todoService.deleteByTodoNo(todoNo);

        return ApiResponse.createSuccessWithNoContent();
    }
    
}
