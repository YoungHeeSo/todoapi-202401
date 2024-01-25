package com.study.todoapi.todo.repository;

import com.study.todoapi.todo.entity.Todo;
import com.study.todoapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, String> {

    @Query("select t from Todo t where t.user = ?1")
    List<Todo> findAllByUser(User user);

}
