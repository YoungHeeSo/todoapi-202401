package com.study.todoapi.todo.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TodoCheckRequestDTO {

    private String id;
    private boolean done;

}
