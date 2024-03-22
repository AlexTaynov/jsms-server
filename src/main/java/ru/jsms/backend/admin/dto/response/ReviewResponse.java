package ru.jsms.backend.admin.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewResponse {
    private Long versionId;
    private String antiPlagiarism;
    private UUID firstReviewerFileId;
    private UUID secondReviewerFileId;
}
