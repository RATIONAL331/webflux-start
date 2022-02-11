package com.hello.webfluxstart.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
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
