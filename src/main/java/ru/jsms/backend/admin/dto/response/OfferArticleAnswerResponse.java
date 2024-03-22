package ru.jsms.backend.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.admin.entity.OfferArticleAnswer;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class OfferArticleAnswerResponse {
    private Long id;
    private Long versionId;
    private UUID documentId;
    private String comment;
    private boolean isDraft;

    public OfferArticleAnswerResponse(OfferArticleAnswer answer) {
        this.id = answer.getId();
        this.versionId = answer.getVersionId();
        this.documentId = answer.getDocumentId();
        this.comment = answer.getComment();
        this.isDraft = answer.isDraft();
    }
}
