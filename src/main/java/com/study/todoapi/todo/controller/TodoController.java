package com.study.todoapi.todo.controller;

import com.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import com.study.todoapi.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // react 사용 할때
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // 할 일 등록 요청

    // entity는 DB와 밀접한데 클라이언트 사용 x, DTO 사용
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoCreateRequestDTO dto){
        todoService.create(dto);

        return ResponseEntity.ok().body("ok");
    }

}
