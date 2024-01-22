package com.study.todoapi.user.service;


import com.study.todoapi.dto.request.UserSignUpRequestDTO;
import com.study.todoapi.dto.response.UserSignUpResponseDTO;
import com.study.todoapi.exception.NoRegisteredArgumentsException;
import com.study.todoapi.user.entity.User;
import com.study.todoapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponseDTO create(UserSignUpRequestDTO dto){
        if (dto == null){
//            throw  new RuntimeException("회원가입 입력정보가 없습니다");
            throw new NoRegisteredArgumentsException("회원가입 입력정보가 없습니다");
        }
        String email = dto.getEmail();

        if(userRepository.existsByEmail(email)){
            log.warn("이메일이 중복되었습니다!! - {}", email);
            throw  new RuntimeException("중복된 이메일 입니다");
        }

        User saved = userRepository.save(dto.toEntity(passwordEncoder));

        log.info("회원가입 성공!! saved user - {}", saved);

        return new UserSignUpResponseDTO(saved); // 회원가입 정보를 클라이언트에게 리ㅓㄴ
    }

    // 이메일 중복확인
    public boolean isDuplicateEmail(String email){
        return userRepository.existsByEmail(email);
    }


}
