package com.hello.webfluxstart.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String password;
    private List<String> roles;

    public User(String name, String password, List<String> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }
}
