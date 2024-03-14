package ru.jsms.backend.articles.dto.request;

import lombok.Data;

@Data
public class CreateOfferArticleVersionRequest {
    private String articleArchiveId;
    private String documentsArchiveId;
    private String comment;
}
