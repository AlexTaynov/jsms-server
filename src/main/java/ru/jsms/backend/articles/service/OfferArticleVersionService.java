package ru.jsms.backend.articles.service;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.EditOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleVersionResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.articles.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;
import ru.jsms.backend.files.service.FileService;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.OFFER_NOT_COMPLETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.SINGLE_VERSION_DELETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_COMPLETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_DIFFERENT;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_FOUND;
import static ru.jsms.backend.common.utils.BaseOwneredEntityUtils.validateAccess;
import static ru.jsms.backend.common.utils.UuidUtils.parseUuid;

@RequiredArgsConstructor
@Service
public class OfferArticleVersionService {

    private final OfferArticleService offerArticleService;
    private final OfferArticleVersionRepository versionRepository;
    private final OfferArticleRepository offerArticleRepository;
    private final FileService fileService;
    private final HeadersDto headersDto;

    public void submitLastVersion(Long offerArticleId) {
        OfferArticle offerArticle = offerArticleRepository.findById(offerArticleId)
                .orElseThrow(ARTICLE_NOT_FOUND.getException());
        validateAccess(offerArticle, headersDto.getUserId());
        offerArticleService.validateEditAccess(offerArticle);
        if (!offerArticle.isComplete()) {
            throw OFFER_NOT_COMPLETE.getException();
        }

        OfferArticleVersion version = versionRepository.findLastVersionByOfferArticleId(offerArticleId);
        if (!version.isDraft()) {
            return;
        }
        validCompleteVersion(version);

        version.setDraft(false);
        updateStatusToConsideration(offerArticle);
        versionRepository.save(version);
    }

    public OfferArticleVersionResponse getVersion(Long versionId) {
        OfferArticleVersion version = versionRepository.findById(versionId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        validateAccess(version, headersDto.getUserId());
        return convertToResponse(version);
    }

    public PageDto<OfferArticleVersionResponse> getAllVersions(Long offerArticleId, PageParam pageParam) {
        return new PageDto<>(
                versionRepository
                        .findByOfferArticleIdAndOwnerId(offerArticleId, headersDto.getUserId(), pageParam.toPageable())
                        .map(this::convertToResponse)
        );
    }

    public OfferArticleVersionResponse editLastVersion(Long offerArticleId, EditOfferArticleVersionRequest request) {
        OfferArticleVersion version = versionRepository.findLastVersionByOfferArticleId(offerArticleId);
        validateAccess(version, headersDto.getUserId());
        if (request.getArticleArchiveId() != null) {
            fileService.validateAccess(request.getArticleArchiveId());
        }
        if (request.getDocumentsArchiveId() != null) {
            fileService.validateAccess(request.getDocumentsArchiveId());
        }

        if(!version.isDraft()) {
            var offerArticle = version.getOfferArticle();
            version = OfferArticleVersion.builder().offerArticle(offerArticle).ownerId(headersDto.getUserId()).build();
        }
        mapRequestToVersion(request, version);
        return convertToResponse(versionRepository.save(version));
    }

    public void deleteLastVersion(Long offerArticleId) {
        OfferArticleVersion version = versionRepository.findLastVersionByOfferArticleId(offerArticleId);
        validateAccess(version, headersDto.getUserId());
        validateDeleteAccess(version);
        if (versionRepository.countByOfferArticleId(offerArticleId) == 1) {
            throw SINGLE_VERSION_DELETE.getException();
        }
        versionRepository.delete(version);
    }

    public void validateDeleteAccess(OfferArticleVersion version) {
        if (!version.isDraft()) {
            throw EDIT_DENIED.getException();
        }
    }

    private OfferArticleVersionResponse convertToResponse(OfferArticleVersion version) {
        return OfferArticleVersionResponse.builder()
                .id(version.getId())
                .articleArchiveId(version.getArticleArchiveId())
                .documentsArchiveId(version.getDocumentsArchiveId())
                .comment(version.getComment())
                .isDraft(version.isDraft())
                .build();
    }

    private void validCompleteVersion(OfferArticleVersion version) {
        if (version.getArticleArchive() == null || version.getDocumentsArchive() == null) {
            throw VERSION_NOT_COMPLETE.getException();
        }
        OfferArticleVersion previousVersion =
                versionRepository.findLastSubmittedVersion(version.getOfferArticle().getId()).orElse(null);
        if (previousVersion == null) {
            return;
        }
        if (version.getArticleArchiveId().equals(previousVersion.getArticleArchiveId()) &&
                version.getDocumentsArchiveId().equals(previousVersion.getDocumentsArchiveId()) &&
                (StringUtils.isBlank(version.getComment()) ||
                        StringUtils.equals(version.getComment(), previousVersion.getComment()))
        ) {
            throw VERSION_NOT_DIFFERENT.getException();
        }

    }

    private void mapRequestToVersion(EditOfferArticleVersionRequest request, OfferArticleVersion version) {
        version.setArticleArchiveId(parseUuid(request.getArticleArchiveId()));
        version.setDocumentsArchiveId(parseUuid(request.getDocumentsArchiveId()));
        version.setComment(request.getComment());
    }

    private void updateStatusToConsideration(OfferArticle offerArticle) {
        if (offerArticle.getStatus() == OfferArticleStatus.DRAFT) {
            offerArticle.setStatus(OfferArticleStatus.UNDER_CONSIDERATION);
            offerArticleRepository.save(offerArticle);
        }
    }
}
