package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class EditOfferArticleVersionRequest {
    private UUID articleArchiveId;
    private UUID documentsArchiveId;
    private String comment;
}
