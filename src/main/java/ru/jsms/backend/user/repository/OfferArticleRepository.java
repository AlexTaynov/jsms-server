package ru.jsms.backend.user.repository;

import org.springframework.stereotype.Repository;
import ru.jsms.backend.user.entity.OfferArticle;
import ru.jsms.backend.common.repository.BaseOwneredRepository;

@Repository
public interface OfferArticleRepository extends BaseOwneredRepository<OfferArticle, Long> {
}
