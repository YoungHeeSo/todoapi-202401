package com.study.todoapi.auth;

import com.study.todoapi.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {

    private String userId;
    private String email;
    private Role role;

}
