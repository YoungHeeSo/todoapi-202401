package com.study.todoapi.todo.service;

import com.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import com.study.todoapi.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j // 로그 찍을때
@RequiredArgsConstructor
@Transactional // JPA 사용 시 필수
public class TodoService {

    private final TodoRepository todoRepository;

    // 할 일 등록
    public void create(TodoCreateRequestDTO dto){
        todoRepository.save(dto.toEntity());
        log.info("새로운 할 일이 저장되었습니다 : {}", dto.getTitle());
    }

}
