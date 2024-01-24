package com.study.todoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    // 시큐리티 기본 보안설정 해제
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                //세션인증은 더이상 사용하지 않겠다!!
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 어떤 요청에서는 인증을 하고 어떤 요청에서는 인증을 안할 건지 설정
                .authorizeHttpRequests() // 어떤 요청에서 인증을 할 거냐?
                    .antMatchers("/", "/api/auth/**").permitAll() // 이 요청은 인증을 안해도 됨!
//                    .antMatchers(HttpMethod.POST, "/api/todos").permitAll()
//                    .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated() // 그 외의 요청은 인증(로그인)을 받아라!!
        ;

        return http.build();
    }

}
