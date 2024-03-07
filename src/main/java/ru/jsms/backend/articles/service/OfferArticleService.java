package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.enums.PublishingStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
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
    private final AuthService authService;
    private final OfferArticleVersionService versionService;

    public PageDto<OfferArticleResponse> getOfferArticles(PageParam pageParam) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return new PageDto<>(offerArticleRepository.findByOwnerId(userId, pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public OfferArticleResponse createOfferArticle(CreateOfferArticleRequest request) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        OfferArticle offerArticle = offerArticleRepository.save(OfferArticle.builder()
                .name(request.getName())
                .ownerId(userId)
                .build()
        );
        versionService.createDefaultVersion(offerArticle.getId());
        return convertToResponse(offerArticle);
    }

    public void deleteOfferArticle(Long id) {
        offerArticleRepository.findById(id).ifPresent(o -> {
            validateAccess(o);
            if (o.getStatus() != PublishingStatus.DRAFT) {
                throw EDIT_DENIED.getException();
            }
            offerArticleRepository.delete(o);
        });
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle);
        if (offerArticle.getStatus() != PublishingStatus.DRAFT) {
            throw EDIT_DENIED.getException();
        }
        offerArticle.setName(request.getName());
        return convertToResponse(offerArticleRepository.save(offerArticle));
    }

    private void validateAccess(OfferArticle offerArticle) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        if (!offerArticle.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }

    private OfferArticleResponse convertToResponse(OfferArticle offerArticle) {
        return OfferArticleResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .build();
    }
}
