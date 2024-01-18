package com.study.todoapi.todo.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TodoListResponseDTO {

    private String error; // 만약 에러가 발생할 때 메시지 를 저장할 것!

    // todoList 의 key 값을 붙여줄 수 있어서 한번 더 감싼 것
    private List<TodoDetailResponseDTO> todos;
}
