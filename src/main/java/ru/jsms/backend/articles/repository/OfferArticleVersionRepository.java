package ru.jsms.backend.articles.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.articles.entity.OfferArticleVersion;
import ru.jsms.backend.common.repository.BaseOwneredRepository;

@Repository
public interface OfferArticleVersionRepository extends BaseOwneredRepository<OfferArticleVersion, Long> {

    @Transactional(readOnly = true)
    @Query("select o from OfferArticleVersion o where o.offerArticleId = ?1 and o.ownerId = ?2 and o.deleted = false")
    Page<OfferArticleVersion> findByOfferArticleIdAndOwnerId(Long offerArticleId, Long userId, Pageable pageable);

}
