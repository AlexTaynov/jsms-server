package ru.jsms.backend.admin.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ArticleVersionResponse {
    private Long id;
    private UUID articleArchiveId;
    private UUID documentsArchiveId;
    private String comment;
    private OfferArticleAnswerResponse answer;
    private ReviewResponse review;
}
