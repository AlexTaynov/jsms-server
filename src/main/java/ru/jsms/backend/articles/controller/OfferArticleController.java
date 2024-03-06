package ru.jsms.backend.articles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.service.OfferArticleService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offerArticles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class OfferArticleController {

    private final OfferArticleService offerArticleService;

    @GetMapping
    public List<OfferArticleResponse> getOfferArticles() {
        return offerArticleService.getOfferArticles().stream()
                .map(this::convertOfferArticleToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public OfferArticleResponse createOfferArticle(@Valid @RequestBody CreateOfferArticleRequest request) {
        return convertOfferArticleToResponse(offerArticleService.createOfferArticle(request));
    }

    @DeleteMapping("/{id}")
    public void deleteOfferArticle(@PathVariable Long id) {
        offerArticleService.deleteOfferArticle(id);
    }

    @PutMapping("/{id}")
    public OfferArticleResponse editOfferArticle(@PathVariable Long id,
                                                 @Valid @RequestBody EditOfferArticleRequest request) {
        return convertOfferArticleToResponse(offerArticleService.editOfferArticle(id, request));
    }

    private OfferArticleResponse convertOfferArticleToResponse(OfferArticle offerArticle) {
        return OfferArticleResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .build();
    }
}
