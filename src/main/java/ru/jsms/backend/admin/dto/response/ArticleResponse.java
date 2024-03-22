package ru.jsms.backend.admin.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.admin.enums.ArticleStatus;

@Data
@Builder
public class ArticleResponse {
    private Long id;
    private Long offerArticleId;
    private ArticleStatus status;
    private String comment;
}
