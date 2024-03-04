package ru.jsms.backend.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jsms.backend.security.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @Email
    private String email;

    @ValidPassword
    private String password;

    @Size(min = 1, max = 30)
    @NotBlank
    private String firstName;

    @Size(min = 1, max = 30)
    @NotBlank
    private String secondName;
}
