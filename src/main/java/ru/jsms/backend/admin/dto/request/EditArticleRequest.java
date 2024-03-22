package ru.jsms.backend.admin.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.admin.enums.ArticleStatus;

@Data
@Builder
public class EditArticleRequest {
    private ArticleStatus status;
    private String comment;
}
