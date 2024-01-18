package com.study.todoapi.todo.dto.response;

import com.study.todoapi.todo.entity.Todo;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TodoDetailResponseDTO {

    private String id;

//    @JsonProperty("todo-title") // key 값이 todo-title으로 바뀜
    private String title;

    private boolean done;

    // 엔터티를 dto으로 바꿔주는 생성자
    public TodoDetailResponseDTO(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
    }
}
