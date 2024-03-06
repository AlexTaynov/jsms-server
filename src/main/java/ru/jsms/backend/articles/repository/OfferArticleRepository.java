package ru.jsms.backend.articles.repository;

import org.springframework.stereotype.Repository;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.common.repository.BaseOwneredRepository;

@Repository
public interface OfferArticleRepository extends BaseOwneredRepository<OfferArticle, Long> {
}
