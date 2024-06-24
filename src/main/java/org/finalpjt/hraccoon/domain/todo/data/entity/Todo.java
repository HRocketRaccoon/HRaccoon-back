package org.finalpjt.hraccoon.domain.todo.data.entity;

import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.global.abstracts.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "todo_no", nullable = false)
	private Long todoNo;

	@Column(name = "todo_content", nullable = false)
	private String todoContent;

	@Column(name = "todo_complete_yn", nullable = false, columnDefinition = "boolean default false")
	private Boolean todoCompleteYn;

	@Column(name = "todo_delete_yn", nullable = false, columnDefinition = "boolean default false")
	private Boolean todoDeleteYn;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_no", nullable = false)
	private User userNo;

	@Builder
	public Todo(User user, String todoContent, Boolean todoCompleteYn, Boolean todoDeleteYn) {
		this.userNo = user;
		this.todoContent = todoContent;
		this.todoCompleteYn = todoCompleteYn;
		this.todoDeleteYn = todoDeleteYn;
	}

	public void updateTodoCompleteYn() {
		this.todoCompleteYn = !this.todoCompleteYn;
	}

	public void updateTodoDeleteYn() {
		this.todoDeleteYn = !this.todoDeleteYn;
	}
}
