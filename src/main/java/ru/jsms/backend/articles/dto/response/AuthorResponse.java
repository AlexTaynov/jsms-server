package ru.jsms.backend.articles.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String email;
}
