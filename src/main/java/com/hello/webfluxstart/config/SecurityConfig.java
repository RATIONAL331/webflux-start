package com.hello.webfluxstart.config;

import com.hello.webfluxstart.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
@EnableWebFluxSecurity
/**
 * EnableWebFluxSecurity이 활성화되면
 * 1. HTTP BASIC을 활성화 하여 cURL같은 도구로 계정명/비밀번호 전송이 가능해진다.
 * 2. HTTP FORM을 활성화 하여 로그인 되지 않은 사용자는 브라우저 기본 로그인 팝업 창 대신에 스프링 시큐리티가 제공하는 로그인 페이지로 리다익렉트 된다.
 * 3. 인증에 성공하면 애플리케이션의 모든 자원에 접근 가능하다.
 */
public class SecurityConfig {
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByName(username)
                                         .map(user -> User.builder()
                                                          .passwordEncoder(s -> PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(s))
                                                          .username(user.getName())
                                                          .password(user.getPassword())
                                                          .authorities(user.getRoles().toArray(new String[0]))
                                                          .build());
    }
}
