package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.AuthorResponse;
import ru.jsms.backend.articles.dto.response.OfferArticleFullResponse;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.Author;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.AuthorRepository;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.articles.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;

import java.util.stream.Collectors;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.AUTHOR_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.common.utils.BaseOwneredEntityUtils.validateAccess;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final OfferArticleRepository offerArticleRepository;
    private final OfferArticleVersionRepository versionRepository;
    private final AuthorRepository authorRepository;
    private final HeadersDto headersDto;

    public PageDto<OfferArticleResponse> getOfferArticles(PageParam pageParam) {
        return new PageDto<>(offerArticleRepository.findByOwnerId(headersDto.getUserId(), pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public OfferArticleFullResponse getOfferArticle(Long id) {
        return convertToFullResponse(offerArticleRepository.findById(id).orElseThrow(ARTICLE_NOT_FOUND.getException()));
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
        validateAccess(offerArticle, headersDto.getUserId());
        validateDeleteAccess(offerArticle);
        versionRepository.deleteAll(offerArticle.getVersions());
        offerArticleRepository.delete(offerArticle);
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto.getUserId());
        validateEditAccess(offerArticle);

        offerArticle.setName(request.getName());
        return convertToResponse(offerArticleRepository.save(offerArticle));
    }

    public void addAuthor(Long offerArticleId, Long authorId) {
        OfferArticle offerArticle = offerArticleRepository.findById(offerArticleId).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto.getUserId());
        validateEditAccess(offerArticle);

        Author author = authorRepository.findById(authorId).orElseThrow(AUTHOR_NOT_FOUND.getException());
        offerArticle.getAuthors().add(author);
        offerArticleRepository.save(offerArticle);
    }

    public void deleteAuthor(Long offerArticleId, Long authorId) {
        OfferArticle offerArticle = offerArticleRepository.findById(offerArticleId).orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto.getUserId());
        validateEditAccess(offerArticle);

        Author author = authorRepository.findById(authorId).orElseThrow(AUTHOR_NOT_FOUND.getException());
        offerArticle.getAuthors().remove(author);
        offerArticleRepository.save(offerArticle);
    }

    public void validateEditAccess(OfferArticle offerArticle) {
        OfferArticleStatus status = offerArticle.getStatus();
        if (status != OfferArticleStatus.DRAFT && status != OfferArticleStatus.UNDER_CONSIDERATION) {
            throw EDIT_DENIED.getException();
        }
    }

    private OfferArticleResponse convertToResponse(OfferArticle offerArticle) {
        return OfferArticleResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .build();
    }

    private OfferArticleFullResponse convertToFullResponse(OfferArticle offerArticle) {
        return OfferArticleFullResponse.builder()
                .id(offerArticle.getId())
                .name(offerArticle.getName())
                .status(offerArticle.getStatus().toString())
                .authors(offerArticle.getAuthors().stream()
                        .map(author -> AuthorResponse.builder()
                                .id(author.getId())
                                .email(author.getEmail())
                                .firstName(author.getFirstName())
                                .secondName(author.getSecondName())
                                .patronymic(author.getPatronymic())
                                .build())
                        .collect(Collectors.toList()))
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
}
