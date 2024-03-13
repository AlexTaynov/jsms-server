package ru.jsms.backend.articles.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OfferArticleVersionResponse {
    private Long id;
    private UUID articleArchiveId;
    private UUID documentsArchiveId;
    private String comment;
    @Builder.Default
    private boolean isDraft = true;
}
