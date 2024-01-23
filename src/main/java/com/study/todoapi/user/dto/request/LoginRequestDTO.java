package com.study.todoapi.user.dto.request;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    private String email;
    private String password;


}
