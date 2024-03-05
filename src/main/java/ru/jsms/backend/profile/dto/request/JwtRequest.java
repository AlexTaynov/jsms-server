package ru.jsms.backend.profile.dto.request;

import lombok.Getter;
import lombok.Setter;
import ru.jsms.backend.profile.validation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class JwtRequest {
    @NotNull
    @Email
    private String email;

    @Password
    private String password;
}
