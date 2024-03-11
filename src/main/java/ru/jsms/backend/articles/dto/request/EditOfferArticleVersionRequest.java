package ru.jsms.backend.articles.dto.request;

import lombok.Data;

@Data
public class EditOfferArticleVersionRequest {
    private String articleArchive;
    private String documentsArchive;
    private String comment;
}
