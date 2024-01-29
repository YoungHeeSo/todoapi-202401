package com.study.todoapi.user.controller;

import com.study.todoapi.auth.TokenUserInfo;
import com.study.todoapi.user.dto.request.LoginRequestDTO;
import com.study.todoapi.user.dto.request.UserSignUpRequestDTO;
import com.study.todoapi.user.dto.response.LoginResponseDTO;
import com.study.todoapi.user.dto.response.UserSignUpResponseDTO;
import com.study.todoapi.exception.DuplicatedEmailException;
import com.study.todoapi.exception.NoRegisteredArgumentsException;
import com.study.todoapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
//@CrossOrigin(origins = {"http://localhost:3000"}) // api 접근을 허용할 클라이언트 ip,3000 포트 허용
public class UserController {

    private final UserService userService;

    // 회원가입 요청처리
    @PostMapping
    public ResponseEntity<?> signUp(
            @Valid @RequestPart("user") UserSignUpRequestDTO dto
            , @RequestPart("profileImage")MultipartFile profileImg
            , BindingResult result
            ){
        log.info("/api/auth POST! - {}", dto);

        if(profileImg != null) log.info("file-name: {}", profileImg.getOriginalFilename());

        if(result.hasErrors()){
            log.warn(result.toString());

            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {
            UserSignUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoRegisteredArgumentsException e) {
            log.warn("필수 가입 정보를 전달받지 못했습니다!!");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicatedEmailException e) {
            log.warn("이메일이 중복되었습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 이메일 중복확인 요청처리
    @GetMapping("/check")
    public ResponseEntity<?> check(String email){
        boolean flag = userService.isDuplicateEmail(email);
        log.debug("{} - 중복?? - {}", email, flag);

        return ResponseEntity.ok().body(flag);
        // 중복이면 true, 아니면 false
    }

    // 로그인 요청 처리
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @Validated @RequestBody LoginRequestDTO dto
    ){

        try{
            LoginResponseDTO responseDTO = userService.authenticate(dto);
            log.info("login success!! by {}", responseDTO.getEmail());

            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
//            return ResponseEntity.status(488).body(e.getMessage());
        }

    }

    // 일반 회원을 프리미엄으로 상승시키는 요청 처리
    @PutMapping("/promote")
    // 이 권한을 가진 사람만 이 요청을 수행할 수 있고
    // 이 권한이 아닌 유저는 강제로 403이 응답 됨.
    @PreAuthorize("hasRole('COMMON')") // 일반 회원만 검사 가능함
//    @PreAuthorize("hasRole(ROLE_COMMON) or hasRole(ROLE_ADMIN") // 일반 회원, 관리자
    public ResponseEntity<?> promote(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        log.info("/api/auth/promote PUT");

        try {
            LoginResponseDTO responseDTO = userService.promoteToPremium(userInfo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return  ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }



}
