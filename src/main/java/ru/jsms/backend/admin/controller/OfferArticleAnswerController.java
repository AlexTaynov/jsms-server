package ru.jsms.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.admin.dto.request.EditOfferArticleAnswerRequest;
import ru.jsms.backend.admin.dto.response.OfferArticleAnswerResponse;
import ru.jsms.backend.admin.service.OfferArticleAnswerService;

@RestController
@RequestMapping("/api/offerArticles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class OfferArticleAnswerController {

    private final OfferArticleAnswerService answerService;

    @PutMapping("/answers/{answerId}/submit")
    public ResponseEntity<Void> submitAnswer(@PathVariable Long answerId) {
        answerService.submit(answerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/answers")
    public ResponseEntity<OfferArticleAnswerResponse> editAnswer(@RequestParam Long versionId,
                                                                 @RequestBody EditOfferArticleAnswerRequest request) {
        return ResponseEntity.ok(answerService.editAnswer(versionId, request));
    }
}
