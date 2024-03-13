package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleVersionResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.articles.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;
import ru.jsms.backend.common.utils.BaseOwneredEntityUtils;
import ru.jsms.backend.profile.service.AuthService;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ARTICLE_NOT_FOUND;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.DRAFT_ALREADY_EXISTS;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.SINGLE_VERSION_DELETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_COMPLETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class OfferArticleVersionService {

    private final OfferArticleService offerArticleService;
    private final OfferArticleVersionRepository versionRepository;
    private final OfferArticleRepository offerArticleRepository;
    private final AuthService authService;

    public OfferArticleVersionResponse createVersion(Long offerArticleId, CreateOfferArticleVersionRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(offerArticleId)
                .orElseThrow(ARTICLE_NOT_FOUND.getException());
        BaseOwneredEntityUtils.validateAccess(offerArticle);
        offerArticleService.validateEditAccess(offerArticle);

        checkIfDraftVersionAlreadyExists(offerArticleId);

        OfferArticleVersion offerArticleVersion = OfferArticleVersion.builder()
                .offerArticle(offerArticle)
                .articleArchiveId(request.getArticleArchiveId())
                .documentsArchiveId(request.getDocumentsArchiveId())
                .comment(request.getComment())
                .ownerId(offerArticle.getOwnerId())
                .build();
        return convertToResponse(versionRepository.save(offerArticleVersion));
    }

    public void submitLastVersion(Long offerArticleId) {
        OfferArticle offerArticle = offerArticleRepository.findById(offerArticleId)
                .orElseThrow(ARTICLE_NOT_FOUND.getException());
        BaseOwneredEntityUtils.validateAccess(offerArticle);
        offerArticleService.validateEditAccess(offerArticle);

        OfferArticleVersion version = versionRepository.findLastVersionByOfferArticleId(offerArticleId).get();
        checkVersionIsComplete(version);
        version.setDraft(false);
        updateStatusToConsideration(offerArticle);
        versionRepository.save(version);
    }

    public OfferArticleVersionResponse getVersion(Long versionId) {
        OfferArticleVersion version = versionRepository.findById(versionId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        BaseOwneredEntityUtils.validateAccess(version);
        return convertToResponse(version);
    }

    public PageDto<OfferArticleVersionResponse> getAllVersions(Long offerArticleId, PageParam pageParam) {
        final Long userId = authService.getUserId();
        return new PageDto<>(
                versionRepository.findByOfferArticleIdAndOwnerId(offerArticleId, userId, pageParam.toPageable())
                        .map(this::convertToResponse)
        );
    }

    public OfferArticleVersionResponse editLastVersion(Long offerArticleId, EditOfferArticleVersionRequest request) {
        OfferArticleVersion version = versionRepository.findLastVersionByOfferArticleId(offerArticleId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        BaseOwneredEntityUtils.validateAccess(version);
        validateEditAccess(version);

        mapRequestToVersion(request, version);
        return convertToResponse(versionRepository.save(version));
    }

    public void deleteLastVersion(Long offerArticleId) {
        OfferArticleVersion version =
                versionRepository.findLastVersionByOfferArticleId(offerArticleId).orElse(null);
        if (version == null)
            return;
        BaseOwneredEntityUtils.validateAccess(version);
        validateEditAccess(version);
        if (versionRepository.countByOfferArticleId(offerArticleId) == 1) {
            throw SINGLE_VERSION_DELETE.getException();
        }
        versionRepository.delete(version);
    }

    public void validateEditAccess(OfferArticleVersion version) {
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

    private void checkVersionIsComplete(OfferArticleVersion version) {
        if (version.getArticleArchive() == null || version.getDocumentsArchive() == null) {
            throw VERSION_NOT_COMPLETE.getException();
        }
    }

    private void mapRequestToVersion(EditOfferArticleVersionRequest request, OfferArticleVersion version) {
        version.setArticleArchiveId(request.getArticleArchiveId());
        version.setDocumentsArchiveId(request.getDocumentsArchiveId());
        version.setComment(request.getComment());
    }

    private void checkIfDraftVersionAlreadyExists(Long offerArticleId) {
        versionRepository.findLastVersionByOfferArticleId(offerArticleId).ifPresent(version -> {
            if (version.isDraft())
                throw DRAFT_ALREADY_EXISTS.getException();
        });
    }

    private void updateStatusToConsideration(OfferArticle offerArticle) {
        if (offerArticle.getStatus() == OfferArticleStatus.DRAFT) {
            offerArticle.setStatus(OfferArticleStatus.UNDER_CONSIDERATION);
            offerArticleRepository.save(offerArticle);
        }
    }
}
