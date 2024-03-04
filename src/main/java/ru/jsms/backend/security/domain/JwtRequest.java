package ru.jsms.backend.security.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class JwtRequest {
    @NotNull
    @Email
    private String email;
    @NotEmpty
    private String password;

}
