package org.finalpjt.hraccoon.domain.todo.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.todo.data.entity.Todo;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Todo findByTodoNo(Long todoNo);

    List<Todo> findAllByUserNo(User user);

}
