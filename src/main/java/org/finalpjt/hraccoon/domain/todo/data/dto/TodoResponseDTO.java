package org.finalpjt.hraccoon.domain.todo.data.dto;

import java.util.List;

import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;

@Getter
public class TodoResponseDTO {
    private Long todoNo;

    private Long userNo;

	private String todoContent;

	private Boolean todoCompleteYn;

	private Boolean todoDeleteYn;

	private String userId;

	private String userName;
	public TodoResponseDTO(Todo params) {
		this.todoNo = params.getTodoNo();
		this.todoContent = params.getTodoContent();
		this.todoCompleteYn = params.getTodoCompleteYn();
		this.todoDeleteYn = params.getTodoDeleteYn();
	}

    public static List<TodoResponseDTO> toDtoList(List<Todo> todoList) {
		return todoList.stream().map(TodoResponseDTO::new).toList();
	}

	public void setUserInfo(User params) {
		this.userNo = params.getUserNo();
		this.userId = params.getUserId();
		this.userName = params.getUserName();
	}



    
}
