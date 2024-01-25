package com.study.todoapi.todo.controller;

import com.study.todoapi.auth.TokenUserInfo;
import com.study.todoapi.todo.dto.request.TodoCheckRequestDTO;
import com.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import com.study.todoapi.todo.dto.response.TodoListResponseDTO;
import com.study.todoapi.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController // react 사용 할때
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@CrossOrigin(origins = {"http://localhost:3000"}) // api 접근을 허용할 클라이언트 ip,3000 포트 허용
public class TodoController {

    private final TodoService todoService;

    // 할 일 등록 요청
    // entity는 DB와 밀접한데 클라이언트 사용 x, DTO 사용
    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo, // 토큰에 들어있는 파싱된 로그인 유저 정보를 시큐리티가 주입해줌
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
            TodoListResponseDTO dtoList = todoService.create(dto, userInfo.getEmail());
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
    public ResponseEntity<?> retrieveTodoList(
            @AuthenticationPrincipal TokenUserInfo userInfo // 토큰에 들어있는 파싱된 로그인 유저 정보를 시큐리티가 주입해줌
    ){
        log.info("/api/todos GET!");

        TodoListResponseDTO retrieve = todoService.retrieve(userInfo.getEmail());

        return ResponseEntity.ok().body(retrieve);
    }

    // 할 일 삭제 요청 처리
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id, String email){

        log.info("/api/todos/{} DELETE !!", id);

        if(id == null || id.trim().equals("")){
            return ResponseEntity.badRequest()
                    .body(TodoListResponseDTO
                            .builder()
                            .error("ID는 공백일 수 없습니다")
                            .build());
        }

        try {
            TodoListResponseDTO dtoList = todoService.delete(id, email);
            return ResponseEntity.ok().body(dtoList);

        } catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()).build());
        }

    }

    // 할 일 완료 체크 처리 요청
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> updateTodo(
            @RequestBody TodoCheckRequestDTO dto
            , HttpServletRequest request
            , String email
    ){

        log.info("/api/todos {}", request.getMethod());
        log.debug("dto: {}", dto);

        try {
            TodoListResponseDTO dtoList = todoService.check(dto, email);
            return ResponseEntity.ok().body(dtoList);

        } catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder()
                            .error(e.getMessage())
                            .build());
        }
    }

}
