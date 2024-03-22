package ru.jsms.backend.user.dto.request;

import lombok.Data;

@Data
public class EditOfferArticleVersionRequest {
    private String articleArchiveId;
    private String documentsArchiveId;
    private String comment;
}
