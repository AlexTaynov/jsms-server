package ru.jsms.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.admin.dto.request.EditReviewRequest;
import ru.jsms.backend.admin.dto.response.ReviewResponse;
import ru.jsms.backend.admin.service.ReviewService;

@RestController
@RequestMapping("/api/admin/offerArticles/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ReviewController {

    private final ReviewService reviewService;

    @PutMapping
    public ResponseEntity<ReviewResponse> editReview(@RequestParam Long versionId,
                                                     @RequestBody EditReviewRequest request) {
        return ResponseEntity.ok(reviewService.editReview(versionId, request));
    }
}
