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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.service.OfferArticleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/offerArticles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class OfferArticleController {

    private final OfferArticleService offerArticleService;

    @GetMapping
    public ResponseEntity<List<OfferArticleResponse>> getOfferArticles(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ResponseEntity.ok(offerArticleService.getOfferArticles(page, size));
    }

    @PostMapping
    public ResponseEntity<OfferArticleResponse> createOfferArticle(
            @Valid @RequestBody CreateOfferArticleRequest request) {
        return ResponseEntity.ok(offerArticleService.createOfferArticle(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferArticle(@PathVariable Long id) {
        offerArticleService.deleteOfferArticle(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferArticleResponse> editOfferArticle(@PathVariable Long id,
                                                 @Valid @RequestBody EditOfferArticleRequest request) {
        return ResponseEntity.ok(offerArticleService.editOfferArticle(id, request));
    }
}
