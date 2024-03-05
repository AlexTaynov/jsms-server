package ru.jsms.backend.security.domain;

import lombok.Getter;
import lombok.Setter;
import ru.jsms.backend.security.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class JwtRequest {
    @NotNull
    @Email
    private String email;

    @ValidPassword
    private String password;
}
