package ru.jsms.backend.admin.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OfferArticleAnswerResponse {
    private Long id;
    private Long versionId;
    private UUID documentId;
    private String comment;
    private boolean isDraft;
}
