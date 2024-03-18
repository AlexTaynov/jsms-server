package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateAuthorRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    private String patronymic;

    @NotNull
    @Email
    private String email;

}
