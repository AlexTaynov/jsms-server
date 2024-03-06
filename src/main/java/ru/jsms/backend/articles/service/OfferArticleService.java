package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.dto.response.OfferArticleResponse;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.profile.service.AuthService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ACCESS_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final OfferArticleRepository offerArticleRepository;
    private final AuthService authService;

    public List<OfferArticleResponse> getOfferArticles(Integer page, Integer size) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        Pageable pageable;
        if (page != null && size != null) {
            pageable = PageRequest.of(page, size);
        }
        else {
            pageable = Pageable.unpaged();
        }
        return offerArticleRepository.findByOwnerId(userId, pageable).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OfferArticleResponse createOfferArticle(CreateOfferArticleRequest request) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return convertToResponse(offerArticleRepository.save(OfferArticle.builder()
                .name(request.getName())
                .ownerId(userId)
                .build()
        ));
    }

    public void deleteOfferArticle(Long id) {
        offerArticleRepository.findById(id).ifPresent(o -> {
            validateAccess(o);
            offerArticleRepository.delete(o);
        });
    }

    public OfferArticleResponse editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow();
        validateAccess(offerArticle);
        offerArticle.setName(request.getName());
        return convertToResponse(offerArticleRepository.save(offerArticle));
    }

    private void validateAccess(OfferArticle offerArticle) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        if (!offerArticle.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
        if (offerArticle.getStatus() != OfferArticleStatus.DRAFT) {
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
}
