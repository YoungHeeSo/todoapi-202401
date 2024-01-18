package com.study.todoapi.todo.controller;

import com.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import com.study.todoapi.todo.dto.response.TodoListResponseDTO;
import com.study.todoapi.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> createTodo(
            @Validated // 값 검증할 거야
            @RequestBody
            TodoCreateRequestDTO dto
            , BindingResult result
    ){
        // 검증 처리
        if(result.hasErrors()){
            log.warn("DTO 검증 에러!! : {}", result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        // 400, 500 오류 따로 처리하기
        try{
            TodoListResponseDTO dtoList = todoService.create(dto);
            return  ResponseEntity.ok().body(dtoList);
        }catch (Exception e){
            log.error(e.getMessage());
            return  ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()).build()); // TodoListResponseDTO 에러 저장
        }

    }

    // 할 일 목록 조회 요청
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        log.info("/api/todos GET!");

        TodoListResponseDTO retrieve = todoService.retrieve();

        return ResponseEntity.ok().body(retrieve);
    }

}
