package ru.jsms.backend.admin.dto.request;

import lombok.Data;

@Data
public class EditOfferArticleAnswerRequest {
    private String documentId;
    private String comment;
}
