package com.study.todoapi.user.service;


import com.study.todoapi.auth.TokenProvider;
import com.study.todoapi.auth.TokenUserInfo;
import com.study.todoapi.user.dto.request.LoginRequestDTO;
import com.study.todoapi.user.dto.request.UserSignUpRequestDTO;
import com.study.todoapi.user.dto.response.LoginResponseDTO;
import com.study.todoapi.user.dto.response.UserSignUpResponseDTO;
import com.study.todoapi.exception.NoRegisteredArgumentsException;
import com.study.todoapi.user.entity.Role;
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
    private final TokenProvider tokenProvider;

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

    // 회원 인증 (login)
    public LoginResponseDTO authenticate (final LoginRequestDTO dto){

        // 이메일을 통해 회원정보를 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("가입된 회원이 아닙니다!")
                );

        // 패스워드 검증
        String inputPassword =  dto.getPassword(); // 입력 비번
        String encodedPassword =  user.getPassword(); // DB에 저장된 비번

        if(!passwordEncoder.matches(inputPassword, encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }

        // 로그인 성공 후 이제 어떻게 할 것인가? 세션에 저장? 토큰 발급할 것인가?
        String token = tokenProvider.createToken(user);

        // 클라이언트에게 토큰을 발급해서 제공
        return new LoginResponseDTO(user, token);

    }

    // 등급 업 처리
    public LoginResponseDTO promoteToPremium(TokenUserInfo userInfo){
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(
                        () -> new NoRegisteredArgumentsException("가입된 회원이 아닙니다.")
                );

        // 이미 프리미엄회원이거나 관리자면 예외 발생
        if(user.getRole() != Role.COMMON){
            throw new IllegalStateException("일반 회원이 아니면 승격할 수 없습니다");
        }

        // 등급 변경
        user.setRole(Role.PREMIUM);
        User saved = userRepository.save(user);

        // 토큰을 재발급
        String token = tokenProvider.createToken(saved);

        return new LoginResponseDTO(saved, token);
    }


}
