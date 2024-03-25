package ru.jsms.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.admin.dto.request.EditOfferArticleAnswerRequest;
import ru.jsms.backend.admin.dto.response.OfferArticleAnswerResponse;
import ru.jsms.backend.admin.service.OfferArticleAnswerService;

@RestController
@RequestMapping("/api/admin/offerArticles/answers")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class OfferArticleAnswerController {

    private final OfferArticleAnswerService answerService;

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@RequestParam Long versionId) {
        answerService.submit(versionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<OfferArticleAnswerResponse> editAnswer(@RequestParam Long versionId,
                                                                 @RequestBody EditOfferArticleAnswerRequest request) {
        return ResponseEntity.ok(answerService.editAnswer(versionId, request));
    }
}
