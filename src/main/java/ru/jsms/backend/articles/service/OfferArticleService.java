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
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.common.utils.BaseOwneredEntityUtils.validateAccess;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final OfferArticleRepository offerArticleRepository;
    private final OfferArticleVersionRepository versionRepository;
    private final HeadersDto headersDto;

    public PageDto<OfferArticleResponse> getOfferArticles(PageParam pageParam) {
        return new PageDto<>(offerArticleRepository.findByOwnerId(headersDto.getUserId(), pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public OfferArticleResponse createOfferArticle(CreateOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.save(
                OfferArticle.builder()
                        .name(request.getName())
                        .ownerId(headersDto.getUserId())
                        .build()
        );
        createDefaultVersion(offerArticle);
        return convertToResponse(offerArticle);
    }

    public void deleteOfferArticle(Long id) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElse(null);
        if (offerArticle == null) {
            return;
        }
        validateAccess(offerArticle, headersDto);
        validateDeleteAccess(offerArticle);
        versionRepository.deleteAll(offerArticle.getVersions());
        offerArticleRepository.delete(offerArticle);
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto);
        validateEditAccess(offerArticle);

        offerArticle.setName(request.getName());
        return convertToResponse(offerArticleRepository.save(offerArticle));
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
        versionRepository.save(
                OfferArticleVersion.builder()
                        .offerArticle(offerArticle)
                        .ownerId(headersDto.getUserId())
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
