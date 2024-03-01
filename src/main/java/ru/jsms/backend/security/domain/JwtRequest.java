package ru.jsms.backend.security.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    private String email;
    private String password;

}
