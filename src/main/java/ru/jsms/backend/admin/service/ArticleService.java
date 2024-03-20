package ru.jsms.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.admin.entity.Article;
import ru.jsms.backend.admin.repository.ArticleRepository;
import ru.jsms.backend.user.entity.OfferArticle;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public void createArticle(OfferArticle offerArticle) {
        var article = Article.builder().offerArticle(offerArticle).build();
        articleRepository.save(article);
    }
}
