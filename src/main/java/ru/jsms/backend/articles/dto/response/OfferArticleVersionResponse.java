package ru.jsms.backend.articles.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferArticleVersionResponse {
    private Long id;
    private String articleArchive;
    private String documentsArchive;
    private String comment;
    @Builder.Default
    private boolean isDraft = true;
}
