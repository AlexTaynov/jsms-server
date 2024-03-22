package ru.jsms.backend.admin.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class EditReviewRequest {
    private String antiPlagiarism;
    private UUID firstReviewerFileId;
    private UUID secondReviewerFileId;
}
