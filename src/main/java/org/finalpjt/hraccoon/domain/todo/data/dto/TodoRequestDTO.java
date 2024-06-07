package org.finalpjt.hraccoon.domain.todo.data.dto;

import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoRequestDTO {

    @NotBlank
	private String todoContent;

	@NotNull
	private Boolean todoCompleteYn;

	@NotNull
	private Boolean todoDeleteYn;

	@NotNull
	private Long userNo;

	public Todo toEntity(TodoRequestDTO data, User user) {
		return Todo.builder()
			.user(user)
			.todoContent(data.getTodoContent())
			.todoCompleteYn(data.getTodoCompleteYn())
			.todoDeleteYn(data.getTodoDeleteYn())
			.build();
	}
    
}
