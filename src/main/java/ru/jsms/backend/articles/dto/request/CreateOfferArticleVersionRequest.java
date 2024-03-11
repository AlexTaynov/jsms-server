package ru.jsms.backend.articles.dto.request;

import lombok.Data;

@Data
public class CreateOfferArticleVersionRequest {
    private String articleArchive;
    private String documentsArchive;
    private String comment;
}
