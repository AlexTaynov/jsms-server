package ru.jsms.backend.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.admin.entity.Review;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private String antiPlagiarism;
    private UUID firstReviewerFileId;
    private UUID secondReviewerFileId;

    public ReviewResponse(Review review) {
        this.antiPlagiarism = review.getAntiPlagiarism();
        this.firstReviewerFileId = review.getFirstReviewerFileId();
        this.secondReviewerFileId = review.getSecondReviewerFileId();
    }
}
