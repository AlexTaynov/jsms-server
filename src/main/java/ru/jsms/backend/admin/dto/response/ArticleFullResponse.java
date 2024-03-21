package ru.jsms.backend.admin.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.admin.enums.ArticleStatus;
import ru.jsms.backend.user.dto.response.AuthorResponse;

import java.util.Set;

@Data
@Builder
public class ArticleFullResponse {
    private Long id;
    private Long offerArticleId;
    private String name;
    private ArticleStatus status;
    private String comment;
    private Set<AuthorResponse> authors;
}
