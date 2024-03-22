package ru.jsms.backend.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthorFullResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String email;
    private List<OfferArticleResponse> articles;
}
