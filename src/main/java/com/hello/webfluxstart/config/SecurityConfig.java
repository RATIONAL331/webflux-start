package com.hello.webfluxstart.config;

import com.hello.webfluxstart.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
/**
 * EnableWebFluxSecurity이 활성화되면
 * 1. HTTP BASIC을 활성화 하여 cURL같은 도구로 계정명/비밀번호 전송이 가능해진다.
 * 2. HTTP FORM을 활성화 하여 로그인 되지 않은 사용자는 브라우저 기본 로그인 팝업 창 대신에 스프링 시큐리티가 제공하는 로그인 페이지로 리다익렉트 된다.
 * 3. 인증에 성공하면 애플리케이션의 모든 자원에 접근 가능하다.
 */
public class SecurityConfig {
    public static final String USER = "USER";
    public static final String INVENTORY = "INVENTORY";

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

    @Bean
    public SecurityWebFilterChain myCustomSecurityPolicy(ServerHttpSecurity httpSecurity) {
        return httpSecurity.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers(HttpMethod.POST, "/**")
                                                                                            .hasRole(INVENTORY)
                                                                                            // '/**'로 들오오는 POST 요청은 ROLE_INVENTORY라는 역할을 가진 사용자로부터 전송되었을 때만 허용
                                                                                            .pathMatchers(HttpMethod.DELETE, "/**")
                                                                                            .hasRole(INVENTORY)
                                                                                            // '/**'로 들어오는 DELETE 요청은 ROLE_INVENTORY라는 역할을 가진 사용자로부터 전송되었을 때만 허용
                                                                                            .anyExchange().authenticated()
                                                                                            // 규칙에 어긋나는 모든 요청은 더 이상 전진 불가
                                                                                            .and()
                                                                                            .httpBasic()
                                                                                            // HTTP BASIC 인증 허용 => SSL로 보호된 요청을 사용하지 않으면 비밀번호가 탈취될 수 있다.
                                                                                            .and()
                                                                                            .formLogin())
                           .csrf()
                           .disable()
                           .build();
    }
}
