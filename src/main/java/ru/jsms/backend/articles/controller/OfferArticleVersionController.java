package ru.jsms.backend.articles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleVersionResponse;
import ru.jsms.backend.articles.service.OfferArticleVersionService;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/offerArticles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class OfferArticleVersionController {

    private final OfferArticleVersionService versionService;

    @PostMapping("/{offerArticleId}/versions")
    public ResponseEntity<OfferArticleVersionResponse> createVersion(
            @PathVariable Long offerArticleId,
            @RequestBody CreateOfferArticleVersionRequest request
    ) {
        return ResponseEntity.ok(versionService.createVersion(offerArticleId, request));
    }

    @PutMapping("/{offerArticleId}/versions/submit")
    public ResponseEntity<Void> submitLastVersion(@PathVariable Long offerArticleId) {
        versionService.submitLastVersion(offerArticleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/versions/{versionId}")
    public ResponseEntity<OfferArticleVersionResponse> getVersion(@PathVariable Long versionId) {
        return ResponseEntity.ok(versionService.getVersion(versionId));
    }

    @GetMapping("/{offerArticleId}/versions")
    public ResponseEntity<PageDto<OfferArticleVersionResponse>> getAllVersions(@PathVariable Long offerArticleId,
                                                                               PageParam pageParam) {
        return ResponseEntity.ok(versionService.getAllVersions(offerArticleId, pageParam));
    }

    @PutMapping("/{offerArticleId}/versions")
    public ResponseEntity<OfferArticleVersionResponse> editLastVersion(@PathVariable Long offerArticleId,
                                                       @Valid @RequestBody EditOfferArticleVersionRequest request) {
        return ResponseEntity.ok(versionService.editLastVersion(offerArticleId, request));
    }

    @DeleteMapping("/{offerArticleId}/versions")
    public ResponseEntity<Void> deleteLastVersion(@PathVariable Long offerArticleId) {
        versionService.deleteLastVersion(offerArticleId);
        return ResponseEntity.ok().build();
    }
}
