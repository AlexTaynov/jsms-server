package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOfferArticleVersionRequest {
    @NotBlank
    private String articleArchive;
    private String documentsArchive;
    private String comment;
}
