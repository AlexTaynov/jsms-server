package ru.jsms.backend.articles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.jsms.backend.articles.dto.request.CreateOfferArticleRequest;
import ru.jsms.backend.articles.dto.request.EditOfferArticleRequest;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.articles.repository.OfferArticleRepository;
import ru.jsms.backend.profile.service.AuthService;

import java.util.List;

import static ru.jsms.backend.articles.enums.ArticleExceptionCode.ACCESS_DENIED;
import static ru.jsms.backend.articles.enums.ArticleExceptionCode.EDIT_DENIED;

@RequiredArgsConstructor
@Service
public class OfferArticleService {

    private final OfferArticleRepository offerArticleRepository;
    private final AuthService authService;

    public List<OfferArticle> getOfferArticles(Pageable pageable) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return offerArticleRepository.findByOwnerId(userId, pageable);
    }

    public OfferArticle createOfferArticle(CreateOfferArticleRequest request) {
        final Long userId = (Long) authService.getAuthInfo().getPrincipal();
        return offerArticleRepository.save(OfferArticle.builder()
                .name(request.getName())
                .ownerId(userId)
                .build()
        );
    }

    public void deleteOfferArticle(Long id) {
        offerArticleRepository.findById(id).ifPresent(o -> {
            validateAccess(o);
            offerArticleRepository.delete(o);
        });
    }

    public OfferArticle editOfferArticle(Long id, EditOfferArticleRequest request) {
        OfferArticle offerArticle = offerArticleRepository.findById(id).orElseThrow();
        validateAccess(offerArticle);
        offerArticle.setName(request.getName());
        return offerArticleRepository.save(offerArticle);
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
}
