package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleVersionRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleVersionResponse;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.articles.repository.OfferArticleVersionRepository;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;
import ru.jsms.backend.profile.service.AuthService;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ACCESS_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.SINGLE_VERSION_DELETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_COMPLETE;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.VERSION_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class OfferArticleVersionService {

    private final OfferArticleVersionRepository versionRepository;
    private final AuthService authService;

    public OfferArticleVersionResponse createVersion(Long offerArticleId, CreateOfferArticleVersionRequest request) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return convertToResponse(versionRepository.save(OfferArticleVersion.builder()
                .offerArticleId(offerArticleId)
                .articleArchive(request.getArticleArchive())
                .documentsArchive(request.getDocumentsArchive())
                .comment(request.getComment())
                .ownerId(userId)
                .build()));
    }

    public void createDefaultVersion(Long offerArticleId) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        versionRepository.save(OfferArticleVersion.builder()
                .offerArticleId(offerArticleId)
                .ownerId(userId)
                .build());
    }

    public void submitVersion(Long versionId) {
        OfferArticleVersion version = versionRepository.findById(versionId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        validateAccess(version);
        if (version.getArticleArchive() == null) {
            throw VERSION_NOT_COMPLETE.getException();
        }
        version.setDraft(false);
        versionRepository.save(version);
    }

    public OfferArticleVersionResponse getVersion(Long versionId) {
        OfferArticleVersion version = versionRepository.findById(versionId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        validateAccess(version);
        return convertToResponse(version);
    }

    public PageDto<OfferArticleVersionResponse> getAllVersions(Long offerArticleId, PageParam pageParam) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return new PageDto<>(
                versionRepository.findByOfferArticleIdAndOwnerId(offerArticleId, userId, pageParam.toPageable())
                .map(this::convertToResponse)
        );
    }

    public OfferArticleVersionResponse editVersion(Long versionId, EditOfferArticleVersionRequest request) {
        OfferArticleVersion version = versionRepository.findById(versionId)
                .orElseThrow(VERSION_NOT_FOUND.getException());
        validateAccess(version);
        if (!version.isDraft()) {
            throw EDIT_DENIED.getException();
        }

        if (request.getArticleArchive() != null) {
            version.setArticleArchive(request.getArticleArchive());
        }
        if (request.getDocumentsArchive() != null) {
            version.setDocumentsArchive(request.getDocumentsArchive());
        }
        if (request.getComment() != null) {
            version.setComment(request.getComment());
        }
        return convertToResponse(versionRepository.save(version));
    }

    public void deleteVersion(Long versionId) {
        versionRepository.findById(versionId).ifPresent(v -> {
            validateAccess(v);
            if (!v.isDraft()) {
                throw EDIT_DENIED.getException();
            }
            if (v.getOfferArticle().getVersions().size() == 1) {
                throw SINGLE_VERSION_DELETE.getException();
            }
            versionRepository.delete(v);
        });
    }

    private void validateAccess(OfferArticleVersion version) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        if (!version.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }

    private OfferArticleVersionResponse convertToResponse(OfferArticleVersion version) {
        return OfferArticleVersionResponse.builder()
                .id(version.getId())
                .articleArchive(version.getArticleArchive())
                .documentsArchive(version.getDocumentsArchive())
                .comment(version.getComment())
                .isDraft(version.isDraft())
                .build();
    }
}
