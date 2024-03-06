package ru.jsms.backend.articles.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.articles.entity.OfferArticle;
import ru.jsms.backend.common.repository.BaseRepository;

import java.util.List;

@Repository
public interface OfferArticleRepository extends BaseRepository<OfferArticle, Long> {
    @Transactional(readOnly = true)
    @Query("select e from OfferArticle e where e.ownerId = ?1 and e.deleted = false")
    List<OfferArticle> findByOwnerId(Long ownerId);
}
