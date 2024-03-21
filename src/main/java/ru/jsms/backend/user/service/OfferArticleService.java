package ru.jsms.backend.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.admin.service.ArticleService;
import ru.jsms.backend.user.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.user.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.user.dto.response.AuthorResponse;
import ru.jsms.backend.user.dto.response.OfferArticleFullResponse;
import ru.jsms.backend.user.dto.response.OfferArticleResponse;
import ru.jsms.backend.user.entity.Author;
import ru.jsms.backend.user.entity.OfferArticle;
import ru.jsms.backend.user.entity.OfferArticleVersion;
import ru.jsms.backend.user.enums.OfferArticleStatus;
import ru.jsms.backend.user.repository.AuthorRepository;
import ru.jsms.backend.user.repository.OfferArticleRepository;
import ru.jsms.backend.user.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import java.util.Set;
import java.util.stream.Collectors;

import static ru.jsms.backend.user.enums.ArticleExceptionCode.OFFER_NOT_FOUND;
import static ru.jsms.backend.user.enums.ArticleExceptionCode.AUTHOR_NOT_FOUND;
import static ru.jsms.backend.user.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.common.utils.BaseOwneredEntityUtils.validateAccess;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final ArticleService articleService;
    private final OfferArticleRepository offerArticleRepository;
    private final OfferArticleVersionRepository versionRepository;
    private final AuthorRepository authorRepository;
    private final HeadersDto headersDto;

    public PageDto<OfferArticleResponse> getOfferArticles(PageParam pageParam) {
        return new PageDto<>(offerArticleRepository.findByOwnerId(headersDto.getUserId(), pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public OfferArticleFullResponse getOfferArticle(Long id) {
        return convertToFullResponse(offerArticleRepository.findById(id).orElseThrow(OFFER_NOT_FOUND.getException()));
    }

    public OfferArticleResponse createOfferArticle(CreateOfferArticleRequest request) {
        Set<Author> authors = findAll(request.getAuthorIds());
        OfferArticle offerArticle = offerArticleRepository.save(
                OfferArticle.builder()
                        .name(request.getName())
                        .ownerId(headersDto.getUserId())
                        .authors(authors)
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
        validateAccess(offerArticle, headersDto.getUserId());
        validateDeleteAccess(offerArticle);
        versionRepository.deleteAll(offerArticle.getVersions());
        offerArticleRepository.delete(offerArticle);
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow(OFFER_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto.getUserId());
        validateEditAccess(offerArticle);

        offerArticle.setAuthors(findAll(request.getAuthorIds()));
        if (request.getName() != null) {
            offerArticle.setName(request.getName());
        }
        return convertToResponse(offerArticleRepository.save(offerArticle));
    }

    public void validateEditAccess(OfferArticle offerArticle) {
        OfferArticleStatus status = offerArticle.getStatus();
        if (status != OfferArticleStatus.DRAFT && status != OfferArticleStatus.UNDER_CONSIDERATION) {
            throw EDIT_DENIED.getException();
        }
    }

    private OfferArticleResponse convertToResponse(OfferArticle offerArticle) {
        return new OfferArticleResponse(offerArticle);
    }

    private OfferArticleFullResponse convertToFullResponse(OfferArticle offerArticle) {
        return OfferArticleFullResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .authors(offerArticle.getAuthors().stream().map(AuthorResponse::new).collect(Collectors.toList()))
                .build();
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

    private Set<Author> findAll(Set<Long> authorIds) {
        Set<Author> authors = Set.of();
        if (authorIds != null) {
            authors = authorRepository.findAll(authorIds);
            if (authors.size() != authorIds.size()) {
                throw AUTHOR_NOT_FOUND.getException();
            }
        }
        return authors;
    }

    public void submit(OfferArticle offerArticle) {
        if (offerArticle.getStatus() == OfferArticleStatus.DRAFT) {
            offerArticle.setStatus(OfferArticleStatus.UNDER_CONSIDERATION);
            offerArticleRepository.save(offerArticle);
            articleService.createArticle(offerArticle);
        }
    }
}
