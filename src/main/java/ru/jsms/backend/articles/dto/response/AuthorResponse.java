package ru.jsms.backend.articles.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.articles.entity.Author;

@Data
@AllArgsConstructor
@Builder
public class AuthorResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String email;

    public AuthorResponse(Author author) {
        this.id = author.getId();
        this.firstName = author.getFirstName();
        this.secondName = author.getSecondName();
        this.patronymic = author.getPatronymic();
        this.email = author.getEmail();
    }
}
