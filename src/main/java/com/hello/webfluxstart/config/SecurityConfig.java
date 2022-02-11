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
