package ru.jsms.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.admin.dto.request.EditReviewRequest;
import ru.jsms.backend.admin.dto.response.ReviewResponse;
import ru.jsms.backend.admin.entity.Review;
import ru.jsms.backend.admin.repository.ReviewRepository;
import ru.jsms.backend.files.service.FileService;
import ru.jsms.backend.user.entity.OfferArticleVersion;
import ru.jsms.backend.user.service.OfferArticleVersionService;

import static ru.jsms.backend.user.enums.ArticleExceptionCode.VERSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FileService fileService;
    private final OfferArticleVersionService versionService;

    public ReviewResponse editReview(Long versionId, EditReviewRequest request) {
        if (request.getFirstReviewerFileId() != null) {
            fileService.validateAccess(request.getFirstReviewerFileId());
        }
        if (request.getSecondReviewerFileId() != null) {
            fileService.validateAccess(request.getSecondReviewerFileId());
        }
        OfferArticleVersion version = versionService.findById(versionId).orElseThrow(VERSION_NOT_FOUND.getException());
        Review review = version.getReview();
        if (review == null) {
            review = Review.builder().versionId(versionId).build();
        }
        review.setAntiPlagiarism(request.getAntiPlagiarism());
        review.setFirstReviewerFileId(request.getFirstReviewerFileId());
        review.setSecondReviewerFileId(request.getSecondReviewerFileId());
        return convertToResponse(reviewRepository.save(review));
    }

    private ReviewResponse convertToResponse(Review review) {
        return new ReviewResponse(review);
    }
}
