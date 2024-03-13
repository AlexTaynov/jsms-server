package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.articles.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;
import ru.jsms.backend.profile.service.AuthService;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ACCESS_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final OfferArticleRepository offerArticleRepository;
    private final OfferArticleVersionRepository versionRepository;
    private final AuthService authService;

    public PageDto<OfferArticleResponse> getOfferArticles(PageParam pageParam) {
        final Long userId = authService.getUserId();
        return new PageDto<>(offerArticleRepository.findByOwnerId(userId, pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public OfferArticleResponse createOfferArticle(CreateOfferArticleRequest request) {
        final Long userId = authService.getUserId();
        OfferArticle offerArticle = offerArticleRepository.save(
                OfferArticle.builder()
                        .name(request.getName())
                        .ownerId(userId)
                        .build()
        );
        createDefaultVersion(offerArticle);
        return convertToResponse(offerArticle);
    }

    public void deleteOfferArticle(Long id) {
        offerArticleRepository.findById(id).ifPresent(o -> {
            validateAccess(o);
            validateDeleteAccess(o);
            versionRepository.deleteAll(o.getVersions());
            offerArticleRepository.delete(o);
        });
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle);
        validateEditAccess(offerArticle);

        offerArticle.setName(request.getName());
        return convertToResponse(offerArticleRepository.save(offerArticle));
    }

    public void validateAccess(OfferArticle offerArticle) {
        final Long userId = authService.getUserId();
        if (!offerArticle.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }

    public void validateEditAccess(OfferArticle offerArticle) {
        OfferArticleStatus status = offerArticle.getStatus();
        if (status != OfferArticleStatus.DRAFT && status != OfferArticleStatus.UNDER_CONSIDERATION) {
            throw EDIT_DENIED.getException();
        }
    }

    private void validateDeleteAccess(OfferArticle o) {
        if (o.getStatus() != OfferArticleStatus.DRAFT) {
            throw EDIT_DENIED.getException();
        }
    }

    private void createDefaultVersion(OfferArticle offerArticle) {
        final Long userId = authService.getUserId();
        versionRepository.save(
                OfferArticleVersion.builder()
                        .offerArticle(offerArticle)
                        .ownerId(userId)
                        .build()
        );
    }

    private OfferArticleResponse convertToResponse(OfferArticle offerArticle) {
        return OfferArticleResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .build();
    }
}
