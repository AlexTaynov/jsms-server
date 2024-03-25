package ru.jsms.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.admin.dto.request.EditArticleRequest;
import ru.jsms.backend.admin.dto.response.ArticleFullResponse;
import ru.jsms.backend.admin.dto.response.ArticleResponse;
import ru.jsms.backend.admin.dto.response.ArticleVersionResponse;
import ru.jsms.backend.admin.dto.response.OfferArticleAnswerResponse;
import ru.jsms.backend.admin.dto.response.ReviewResponse;
import ru.jsms.backend.admin.entity.Article;
import ru.jsms.backend.admin.repository.ArticleRepository;
import ru.jsms.backend.common.dto.PageDto;
import ru.jsms.backend.common.dto.PageParam;
import ru.jsms.backend.user.dto.response.AuthorResponse;
import ru.jsms.backend.user.entity.OfferArticle;
import ru.jsms.backend.user.entity.OfferArticleVersion;
import ru.jsms.backend.user.repository.OfferArticleVersionRepository;

import java.util.stream.Collectors;

import static ru.jsms.backend.admin.enums.AdminArticleExceptionCode.ARTICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final OfferArticleVersionRepository versionRepository;

    public void createArticle(OfferArticle offerArticle) {
        var article = Article.builder().offerArticle(offerArticle).build();
        articleRepository.save(article);
    }

    public PageDto<ArticleResponse> getAllArticles(PageParam pageParam) {
        return new PageDto<>(articleRepository.findAll(pageParam.toPageable())
                .map(this::convertToResponse));
    }

    public ArticleFullResponse getArticle(Long articleId) {
        return convertToFullResponse(articleRepository.findById(articleId).orElseThrow(ARTICLE_NOT_FOUND.getException()));
    }

    public ArticleFullResponse editArticle(Long articleId, EditArticleRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow(ARTICLE_NOT_FOUND.getException());
        article.setStatus(request.getStatus());
        article.setComment(request.getComment());
        return convertToFullResponse(articleRepository.save(article));
    }

    public PageDto<ArticleVersionResponse> getArticleVersions(Long articleId, PageParam pageParam) {
        Article article = articleRepository.findById(articleId).orElseThrow(ARTICLE_NOT_FOUND.getException());
        return new PageDto<>(
                versionRepository.findByOfferArticleIdAndDraftIsFalse(
                        article.getOfferArticle().getId(), pageParam.toPageable()).map(this::convertToResponse)
        );
    }

    private ArticleVersionResponse convertToResponse(OfferArticleVersion version) {
        return ArticleVersionResponse.builder()
                .id(version.getId())
                .articleArchiveId(version.getArticleArchiveId())
                .documentsArchiveId(version.getDocumentsArchiveId())
                .comment(version.getComment())
                .answer(new OfferArticleAnswerResponse(version.getAnswer()))
                .review(new ReviewResponse(version.getReview()))
                .build();
    }

    private ArticleResponse convertToResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .offerArticleId(article.getOfferArticle().getId())
                .status(article.getStatus())
                .comment(article.getComment())
                .build();
    }

    private ArticleFullResponse convertToFullResponse(Article article) {
        return ArticleFullResponse.builder()
                .id(article.getId())
                .offerArticleId(article.getOfferArticle().getId())
                .name(article.getOfferArticle().getName())
                .status(article.getStatus())
                .comment(article.getComment())
                .authors(article.getOfferArticle().getAuthors().stream().map(AuthorResponse::new)
                        .collect(Collectors.toSet()))
                .build();
    }
}
